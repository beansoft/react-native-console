package com.github.beansoftapp.reatnative.idea.actions;

import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.utils.RunnerUtil;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;

import javax.swing.*;
import java.io.File;

/**
 * Created by pengwei on 16/9/11.
 */
public class RNRunAction extends BaseAction {

    public RNRunAction() {
        super(PluginIcons.FreelineIcon);
    }

    public RNRunAction(Icon icon) {
        super(icon);
    }

    @Override
    public void actionPerformed() {
//            String python = Utils.getPythonLocation();
//            if (python == null) {
//                NotificationUtils.pythonNotFound();
//            } else {
//                ReactNativeTerminal.getInstance(currentProject).initAndExecute(new String[]{
//                        "echo", "React Native Console by https://github.com/beansoftapp", getArgs()});
//            }

        String gradleLocation = RNPathUtil.getAndroidProjectPath(currentProject.getBasePath());

        if (gradleLocation == null) {
            NotificationUtils.gradleFileNotFound();
            return;
        }

        GeneralCommandLine commandLine = new GeneralCommandLine();
//    ExecutionEnvironment environment = getEnvironment();
        commandLine.setWorkDirectory(gradleLocation);
        commandLine.setExePath("." + File.separator + "gradlew");
        commandLine.addParameters("assembleRelease");
        try {
//            Process process = commandLine.createProcess();
            OSProcessHandler processHandler = createProcessHandler(commandLine);
            RunnerUtil.showHelperProcessRunContent("beansoft", processHandler, currentProject, getExecutor());
            // Run
            processHandler.startNotify();
        } catch (ExecutionException e) {
            e.printStackTrace();
            NotificationUtils.errorNotification("Can't execute command: " + e.getMessage());
        }
    }




//    /**
//     * 设置参数
//     *
//     * @return
//     */
//    protected String getArgs() {
//        return null;
//    }
}
