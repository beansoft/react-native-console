package com.github.beansoftapp.reatnative.idea.utils;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.PathEnvironmentVariableUtil;
import com.intellij.openapi.project.Project;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileFilter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utils for find some dirs.
 * Created by beansoft on 2017/3/14.
 */
public class RNPathUtil {
    static String PACKAGE_JSON = "package.json";
    static String GRADLE_FILE = "build.gradle";

    /**
     * 获取React Native的根目录, 在Android Studio中运行时可能会在android的上一级目录.
     *
     * @param inputDir
     * @return
     */
    public static String getRNProjectPath(Project project, String inputDir) {
        File file = new File(inputDir, PACKAGE_JSON);
        if (file.exists()) {
            return inputDir;
        } else {
            file = new File(inputDir, ".." + File.separatorChar + PACKAGE_JSON);
            if (file.exists()) {
                return inputDir + File.separatorChar + "..";
            }
        }

//        Messages.showWarningDialog(project, "找不到有效的React Native目录, 命令将停止执行.\n目录" +
//                inputDir + "及其上级目录中找不到有效的package.json文件.", "警告");

        return null;
    }

    /**
     * 获取React Native的根目录, 在Android Studio中运行时可能会在android的上一级目录.
     *
     * @param inputDir
     * @return
     */
    public static String getAndroidProjectPath(String inputDir) {
        File file = new File(inputDir, GRADLE_FILE);
        if (file.exists()) {
            return inputDir;
        } else {

            File[] subfolders = new File(inputDir).listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            });

            if (subfolders != null) {
                for (File dir : subfolders) {
                    file = new File(dir.getAbsolutePath() + File.separatorChar + GRADLE_FILE);
                    if (file.exists()) {
                        return dir.getAbsolutePath();
                    }
                }
            }
        }

//        Messages.showWarningDialog(project, "找不到有效的React Native目录, 命令将停止执行.\n目录" +
//                inputDir + "及其上级目录中找不到有效的package.json文件.", "警告");

        return null;
    }

    /**
     * Get the full path of an exe file by the IDEA platform.
     * On Mac sys, PATH might will not be directly access by the IDE code,
     * eg: Android Studio Runtime.exec('adb') will throw a no file or directory exception.
     *
     * @param exeName
     * @return
     */
    public static String getExecuteFileFullPath(String exeName) {
        String fullPath = exeName;
        if (OSUtils.isWindows()) {
            if (!exeName.endsWith(".exe")) {
                // first try exe
                fullPath = getExecuteFullPathSingle(exeName + ".exe");
                if (fullPath != null) {
                    return fullPath;
                }

                if (!exeName.endsWith(".cmd")) {
                    // Fix bug: npm, react-native can't be run Windows: https://github.com/beansoftapp/react-native-console/issues/6
                /*
                Unable to run the commandline:Cannot run program "D:\nodejs\npm" (in directory "D:\Project\wxsh"):
                CreateProcess error=193, %1 不是有效的 Win32 应用程序
                 */
                    fullPath = getExecuteFullPathSingle(exeName + ".cmd");
                    if (fullPath != null) {
                        return fullPath;
                    }
                }

                if (!exeName.endsWith(".bat")) {
                    // Fix bug: gradlew can't be run Windows:
                /*
                Unable to run the commandline:Cannot run program ".\gradlew.cmd"
                 (in directory "D:\Project\wxsh\android"): CreateProcess error=2, 系统找不到指定的文件。
                 there is only a gradlew.bat file, no gradlew.cmd file
                 */
                    fullPath = getExecuteFullPathSingle(exeName + ".bat");
                    if (fullPath != null) {
                        return fullPath;
                    }
                }
            }
        }
        fullPath = getExecuteFullPathSingle(exeName);

        return fullPath;
    }

    public static String getExecuteFullPathSingle(String exeName) {
        List<File> fromPath = PathEnvironmentVariableUtil.findAllExeFilesInPath(exeName);
        if (fromPath != null && fromPath.size() > 0) {
            return fromPath.get(0).toString();
        }
        return null;
    }

    public static String createAdbCommand(String args) {
        String adbFullPath = getExecuteFileFullPath("adb");
        if (adbFullPath != null) {
            return adbFullPath + " " + args;
        }
        return "adb" + " " + args;
    }

    public static String createCommand(String exe, String args) {
        String adbFullPath = getExecuteFileFullPath(exe);
        if (adbFullPath != null) {
            return adbFullPath + " " + args;
        }
        return exe + " " + args;
    }


    public static GeneralCommandLine cmdToGeneralCommandLine(String cmd) {
        GeneralCommandLine commandLine = new GeneralCommandLine(cmd.split(" "));
        commandLine.setCharset(Charset.forName("UTF-8"));
        return commandLine;
    }

    /**
     * Create full path command line.
     * @param shell
     * @param workDirectory - only used on windows for gradlew.bat
     * @return GeneralCommandLine
     */
    public static GeneralCommandLine createFullPathCommandLine(String shell,
                                                               @Nullable String workDirectory) {
        String[] cmds = shell.split(" ");
        String exeFullPath;
        if (cmds.length > 1) {
            String exePath = cmds[0];

            List<String> cmdList = new ArrayList<>();
            cmdList.addAll(Arrays.asList(cmds));
            exeFullPath = getExecuteFileFullPath(exePath);
            if (exeFullPath == null) {
                exeFullPath = exePath;
            }

            // https://github.com/beansoftapp/react-native-console/issues/8
            if (OSUtils.isWindows()) {
                if(exeFullPath.equals("gradlew.bat")) {
                    exeFullPath = workDirectory + File.separator + exeFullPath;
                }
            }

            cmdList.remove(0);
            cmdList.add(0, exeFullPath);

            return new GeneralCommandLine(cmdList);
        } else {
            exeFullPath = getExecuteFileFullPath(shell);
            if (exeFullPath == null) {
                exeFullPath = shell;
            }

            return new GeneralCommandLine(exeFullPath);
        }
    }
}