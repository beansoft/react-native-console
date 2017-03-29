package com.github.beansoftapp.reatnative.idea.utils;

import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationListenerAdapter;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * RN Utility
 *
 * @author beansoft@126.com
 */
public class RNUtil {

    // TODO: 2016/9/13 0013 need refactor tool window
//    private final static String TOOL_ID = "React Native Console";

//    public static void build(Project project) {
//        GeneralCommandLine commandLine = new GeneralCommandLine();
//        commandLine.setWorkDirectory(project.getBasePath());
//        commandLine.setExePath("python");
//        commandLine.addParameter("freeline.py");
//        // debug
//        commandLine.addParameter("-d");
//
//        // commands process
//        try {
//            processCommandline(project, commandLine);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /* process command line */
//    private static void processCommandline(final Project project, GeneralCommandLine commandLine) throws ExecutionException {
//        final OSProcessHandler processHandler = new OSProcessHandler(commandLine);
//        ProcessTerminatedListener.attach(processHandler);
//        processHandler.startNotify();
//
//        ApplicationManager.getApplication().invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                processConsole(project, processHandler);
//            }
//        });
//    }
//
//    /* process attach to console,show the log */
//    // TODO: 2016/9/14 0014 need refactor console method
//    private static void processConsole(Project project, ProcessHandler processHandler) {
//        ConsoleView consoleView = RNUIManager.getInstance(project).getConsoleView(project);
//        consoleView.clear();
//        consoleView.attachToProcess(processHandler);
//
//        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
//        ToolWindow toolWindow;
//        toolWindow = toolWindowManager.getToolWindow(TOOL_ID);
//
//        // if already exist tool window then show it
//        if (toolWindow != null) {
//            toolWindow.show(null);
//            return;
//        }
//
//        toolWindow = toolWindowManager.registerToolWindow(TOOL_ID, true, ToolWindowAnchor.BOTTOM);
//        toolWindow.setTitle("free....");
//        toolWindow.setStripeTitle("Free Console");
//        toolWindow.setShowStripeButton(true);
//        toolWindow.setIcon(PluginIcons.ICON_TOOL_WINDOW);
//        toolWindow.getContentManager().addContent(new ContentImpl(consoleView.getComponent(), "Build", true));
//        toolWindow.show(null);
//    }

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
