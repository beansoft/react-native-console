package com.github.beansoftapp.reatnative.idea.utils;

import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationListenerAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.impl.ContentImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;

/**
 * RN Utility
 *
 * @author beansoft@126.com
 */
public class RNUtil {

    // TODO: 2016/9/13 0013 need refactor tool window
    private final static String TOOL_ID = "React Native Console";


    public static void runGradleCI(Project project, String... params) {
        String path = project.getBasePath();
        String gradleLocation = RNPathUtil.getAndroidProjectPath(path);
        if (gradleLocation == null) {
            NotificationUtils.gradleFileNotFound();
        } else {
            GeneralCommandLine commandLine = new GeneralCommandLine();
//    ExecutionEnvironment environment = getEnvironment();
            commandLine.setWorkDirectory(gradleLocation);
            commandLine.setExePath("." + File.separator + "gradlew");
            commandLine.addParameters(params);

//            try {
////            Process process = commandLine.createProcess();
//                OSProcessHandler processHandler = new KillableColoredProcessHandler(commandLine);
//                RunnerUtil.showHelperProcessRunContent("Update AAR", processHandler, project, DefaultRunExecutor.getRunExecutorInstance());
//                // Run
//                processHandler.startNotify();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//                NotificationUtils.errorNotification("Can't execute command: " + e.getMessage());
//            }

            // commands process
            try {
                processCommandline(project, commandLine);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public static void build(Project project) {
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.setWorkDirectory(project.getBasePath());
        commandLine.setExePath("python");
        commandLine.addParameter("freeline.py");
        // debug
        commandLine.addParameter("-d");

        // commands process
        try {
            processCommandline(project, commandLine);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /* process command line, will very simple console view and tool window */
    private static void processCommandline(final Project project, GeneralCommandLine commandLine) throws ExecutionException {
        final OSProcessHandler processHandler = new OSProcessHandler(commandLine);
        ProcessTerminatedListener.attach(processHandler);
//        processHandler.startNotify();// Don't call this, the command content will not be shown

        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                processConsole(project, processHandler);
            }
        });
    }

    /* process attach to console,show the log */
    // TODO: 2016/9/14 0014 need refactor console method
    private static void processConsole(Project project, ProcessHandler processHandler) {
        ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
        consoleView.clear();
        consoleView.attachToProcess(processHandler);
        processHandler.startNotify();

        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolWindow;
        toolWindow = toolWindowManager.getToolWindow(TOOL_ID);

        // if already exist tool window then show it
        if (toolWindow != null) {
            toolWindow.show(null);
            return;
        }

        toolWindow = toolWindowManager.registerToolWindow(TOOL_ID, true, ToolWindowAnchor.BOTTOM);
        toolWindow.setTitle("Android....");
        toolWindow.setStripeTitle("Android Console");
        toolWindow.setShowStripeButton(true);
        toolWindow.setIcon(PluginIcons.ICON_TOOL_WINDOW);
        toolWindow.getContentManager().addContent(new ContentImpl(consoleView.getComponent(), "Build", false));
        toolWindow.getContentManager().addContent(new ContentImpl(new JButton("Test"), "Build2", false));
        toolWindow.show(null);
    }

    /**
     * if had init freeline return true
     */
    public static boolean hadInitFreeline(Project project) {
        if (project != null) {
            String projectPath = project.getBasePath();
            // freeline directory
            File freelineDir = new File(projectPath, "freeline");
            // freeline.py file
            File freeline_py = new File(projectPath, "freeline.py");
            if (freelineDir.exists() && freeline_py.exists()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 执行./gradlew assembleRelease
     * @param project
     */
    public static void buildAndroid(Project project) {
        GradleUtil.executeTask(project, "assembleRelease", "-Pmirror", new ExternalSystemTaskNotificationListenerAdapter() {
            @Override
            public void onTaskOutput(@NotNull ExternalSystemTaskId id, @NotNull String text, boolean stdOut) {
                super.onTaskOutput(id, text, stdOut);
            }
        });
    }
}
