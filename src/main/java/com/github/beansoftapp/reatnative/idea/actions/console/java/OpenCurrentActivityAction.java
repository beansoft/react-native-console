package com.github.beansoftapp.reatnative.idea.actions.console.java;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleRunAction;
import com.github.beansoftapp.reatnative.idea.exception.ExecutionAdbException;
import com.github.beansoftapp.reatnative.idea.exception.MultipleDevicesAdbException;
import com.github.beansoftapp.reatnative.idea.exception.NoDevicesAdbException;
import com.github.beansoftapp.reatnative.idea.exception.ParseAdbException;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Open Current Activity Android Studio / WebStorm 2017.3+ / IntelliJ Plugin, removed deps on Java plugin,
 * fixed Android 9 activity dump parse issue, improved find file functoin to find the file with correct package.
 * This class includes code from https://github.com/BoD/OpenCurrentActivityIntelliJPlugin/ .
 * @link http://www.jetbrains.org/intellij/sdk/docs/basics/psi_cookbook.html
 * @date 2019-8-6
 * @author beansoft@126.com
 */
public class OpenCurrentActivityAction extends BaseRNConsoleRunAction {
    private static final Logger log = Logger.getInstance(OpenCurrentActivityAction.class);

    private static final String PATTERN_RUN_ACTIVITY = "Run #0";// For Android 9 dumps, added by BeanSoft
    private static final String PATTERN_FOCUSED_ACTIVITY = "mFocusedActivity";
    private static final Pattern PATTERN_ACTIVITY_NAME = Pattern.compile(".* ([a-zA-Z0-9.]+)/([a-zA-Z0-9.]+).*");
    private static final String PATTERN_MULTIPLE_DEVICE = "more than one device";
    private static final String PATTERN_DEVICE_LIST_HEADER = "List";
    private static final Pattern PATTERN_DEVICE_LIST_ITEM = Pattern.compile("(.+)\\p{Space}+(.+)");
    private static final String PATTERN_DEVICE_NOT_FOUND = "device not found";

    private static final String ANDROID_SDK_TYPE_NAME = "Android SDK";
    private static final String ADB_SUBPATH = "/platform-tools/";
    private static final String ADB_WINDOWS = "adb.exe";
    private static final String ADB_UNIX = "adb";
    private static final String UI_NO_SDK_MESSAGE = "Could not find the path for the Android SDK.  Have you configured it?";
    private static final String UI_GENERIC_WARNING = "Warning";
    private static final String EXT_JAVA = ".java";
    private static final String EXT_KOTLIN = ".kt";

    private StatusBar mStatusBar;

    public OpenCurrentActivityAction(ReactNativeConsole terminal) {
        super(terminal, "Open Current Activity",
                "Open Current Activity source code displayed in Android App", AllIcons.General.Locate);
    }

    @Override
    public void doAction(AnActionEvent anActionEvent) {
        mStatusBar = WindowManager.getInstance().getStatusBar(project);
        if (mStatusBar == null) {
            log.warn("mStatusBar is null, which should never happen: give up");
            return;
        }

        String androidSdkPath = null;
        String activityName;
        try {
            activityName = getCurrentActivityName(null, androidSdkPath);
            openActivityFile(project, activityName);
        } catch (MultipleDevicesAdbException e) {
            log.info("Multiple devices detected, get the list and try again");
            try {
                List<String> deviceIdList = getDeviceIdList(androidSdkPath);
                for (String deviceId : deviceIdList) {
                    try {
                        activityName = getCurrentActivityName(deviceId, androidSdkPath);
                        openActivityFile(project, activityName);
                    } catch (ExecutionAdbException e2) {
                        mStatusBar.setInfo("Could not execute adb (" + e2.getCause().getMessage() + ")");
                    } catch (ParseAdbException e2) {
                        mStatusBar.setInfo("Could not parse adb output");
                    } catch (MultipleDevicesAdbException e2) {
                        // This should never happen since we passed a device id
                        log.error("Got a multiple devices message when passing a device id!?", e2);
                        mStatusBar.setInfo("Something went wrong!");
                    }
                }
            } catch (ExecutionAdbException e1) {
                mStatusBar.setInfo("Could not execute adb (" + e1.getCause().getMessage() + ")");
            } catch (ParseAdbException e1) {
                mStatusBar.setInfo("Could not parse adb output");
            } catch (NoDevicesAdbException e1) {
                // This should never happen since we have multiple devices
                log.error("Got a no devices message when passing a device id!?", e1);
                mStatusBar.setInfo("Something went wrong!");
            }
        } catch (ExecutionAdbException e) {
            mStatusBar.setInfo("Could not execute adb (" + e.getCause().getMessage() + ")");
        } catch (ParseAdbException e) {
            mStatusBar.setInfo("Could not parse adb output");
        } catch (NoDevicesAdbException e) {
            mStatusBar.setInfo("Could not find any devices or emulators");
        }
//        openActivityFile(project, "OpenCurrentActivityAction");
    }

    @Override
    protected String command() {
        return null;
    }

    @NotNull
    private String getAdbPath(String androidSdkPath) {
        return RNPathUtil.getExecuteFileFullPath("adb");
    }

    private void openActivityFile(final Project project, String activityName) {
        log.info("activityName=" + activityName);

        String fileName = activityName.replace('.', File.separatorChar);

        // Keep only the class name
        int dotIndex = activityName.lastIndexOf('.');
        if (dotIndex != -1) {
            activityName = activityName.substring(dotIndex + 1);
        }
        final String fileNameJava = activityName + EXT_JAVA;
        final String fileNameKotlin = activityName + EXT_KOTLIN;

        // Open the file
        ApplicationManager.getApplication().invokeLater(
            new Runnable() {
                public void run() {
                    ApplicationManager.getApplication().runReadAction(new Runnable() {
                        public void run() {
                            PsiFile[] foundFiles = FilenameIndex.getFilesByName(project, fileNameJava, GlobalSearchScope.allScope(project));
                            if (foundFiles.length == 0) {
                                log.info("No file with name " + fileNameJava + " found");
//                                mStatusBar.setInfo("Could not find " + fileNameJava + " in project");
                                // Java not found, try Kotlin
                                foundFiles = FilenameIndex.getFilesByName(project, fileNameKotlin, GlobalSearchScope.allScope(project));// PsiShortNamesCache.getInstance(project).getFilesByName(fileNameKotlin);
                            }
                            if (foundFiles.length == 0) {
                                log.info("No file with name " + fileNameKotlin + " found");
                                NotificationUtils.warning("Could not find " +
                                  fileName.replace(File.separatorChar, '.') + " in project");
                                return;
                            }

                            if (foundFiles.length > 1) log.warn("Found more than one file with name " + fileNameJava +
                                " or " + fileNameKotlin);

                            for(PsiFile psiFile : foundFiles) {
                                if( psiFile.getVirtualFile().getCanonicalPath().contains(fileName) ) {
                                    PsiFile foundFile = psiFile;
                                    log.info("Opening file " + foundFile.getName());
                                    OpenFileDescriptor descriptor = new OpenFileDescriptor(project, foundFile.getVirtualFile());
                                    descriptor.navigate(true);
                                    return;
                                }
                            }
                            log.warn("Could not find Source code of " + fileName.replace(File.separatorChar, '.' ));
                          NotificationUtils.errorNotification( "Could not find Source code of " +
                                fileName.replace(File.separatorChar, '.' ));
                        }
                    });
                }
            }
        );
    }

    /**
     * Runs {@code adb shell dumpsys activity activities} and parses the results to retrieve the name of the current foremost Activity.
     *
     * @param androidSdkPath Path of the Android SDK (where to find adb).
     * @return The name of the foremost ("focused") Activity.
     */
    @NotNull
    private String getCurrentActivityName(@Nullable String deviceId, String androidSdkPath)
        throws ExecutionAdbException, MultipleDevicesAdbException, ParseAdbException, NoDevicesAdbException {
        String adbPath = getAdbPath(androidSdkPath);

        ProcessBuilder processBuilder;
        if (deviceId == null) {
            processBuilder = new ProcessBuilder(adbPath, "shell", "dumpsys", "activity", "activities");
        } else {
            processBuilder = new ProcessBuilder(adbPath, "-s", deviceId, "shell", "dumpsys", "activity", "activities");
        }
        processBuilder.redirectErrorStream(true);
        Process process = null;
        try {
            process = processBuilder.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            // Parse for focused activity first
            while ((line = in.readLine()) != null) {
//                log.info("line='" + line + "'");
                if (line.contains(PATTERN_MULTIPLE_DEVICE)) {
                    throw new MultipleDevicesAdbException();
                }
                if (line.contains(PATTERN_DEVICE_NOT_FOUND)) {
                    throw new NoDevicesAdbException();
                }
                if (line.contains(PATTERN_FOCUSED_ACTIVITY)) {
                    Matcher matcher = PATTERN_ACTIVITY_NAME.matcher(line);
                    if (!matcher.matches()) {
                        log.error("Could not find the focused Activity in the line");
                        throw new ParseAdbException("Could not find the focused Activity in the line");
                    }
                    return matcher.group(1) + matcher.group(2);
                }
            }

          if (process != null) process.destroy();

            // Parse for running activity
          process = processBuilder.start();
            in = new BufferedReader(new InputStreamReader(process.getInputStream()));
          while ((line = in.readLine()) != null) {
//                log.info("line='" + line + "'");
            if (line.contains(PATTERN_MULTIPLE_DEVICE)) {
              throw new MultipleDevicesAdbException();
            }
            if (line.contains(PATTERN_DEVICE_NOT_FOUND)) {
              throw new NoDevicesAdbException();
            }

            if (line.contains(PATTERN_RUN_ACTIVITY)) {
              Matcher matcher = PATTERN_ACTIVITY_NAME.matcher(line);
              if (!matcher.matches()) {
                log.error("Could not find the running Activity in the line");
                throw new ParseAdbException("Could not find the running Activity in the line");
              }
              return matcher.group(1) + matcher.group(2);
            }
          }
        } catch (IOException e) {
            log.error("Could not exec adb or read from its process", e);
            throw new ExecutionAdbException(e);
        } finally {
            if (process != null) process.destroy();
        }
        // Reached the end of lines, none of them had info about the focused activity
        throw new ParseAdbException("Could not find the focused Activity in the output");
    }

    /**
     * Runs {@code adb devices} and parses the results to retrieve the list of device ids.
     *
     * @param androidSdkPath Path of the Android SDK (where to find adb).
     * @return The list of device ids.
     */
    @NotNull
    private List<String> getDeviceIdList(String androidSdkPath) throws ExecutionAdbException, ParseAdbException {
        String adbPath = getAdbPath(androidSdkPath);

        ProcessBuilder processBuilder = new ProcessBuilder(adbPath, "devices");
        processBuilder.redirectErrorStream(true);
        Process process = null;
        List<String> res = new ArrayList<String>(4);
        try {
            process = processBuilder.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
//                log.info("line='" + line + "'");
                if (line.contains(PATTERN_DEVICE_LIST_HEADER)) continue;
                Matcher matcher = PATTERN_DEVICE_LIST_ITEM.matcher(line);
                if (!matcher.matches()) {
                    continue;
                }
                String deviceId = matcher.group(1);
                res.add(deviceId);
            }
        } catch (IOException e) {
            log.error("Could not exec adb or read from its process", e);
            throw new ExecutionAdbException(e);
        } finally {
            if (process != null) process.destroy();
        }
        if (res.isEmpty()) {
            // Reached the end of lines, there was no device
            throw new ParseAdbException("Could not find devices in the output");
        }

        return res;
    }

}