package com.github.beansoftapp.reatnative.idea.actions;

import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

import javax.swing.*;

/**
 * Click to display the React Native Console window.
 * Created by beansoft on 2017/5/26.
 */
public class ShowRNConsoleAction extends BaseAction {

    public ShowRNConsoleAction() {
        super(PluginIcons.FreelineIcon);
    }

    public ShowRNConsoleAction(Icon icon) {
        super(icon);
    }

    @Override
    public void actionPerformed() {
        ReactNativeConsole.getInstance(currentProject).initAndActive();
//        System.out.println(
//                System.getProperty("user.home"));
//        System.getProperties().list(System.out);
//
//        GeneralCommandLine commandLine = new GeneralCommandLine();
//        commandLine.setExePath("xcrun");
//        commandLine.addParameters("simctl", "list", "devices", "--json");
//        commandLine.setCharset(Charset.forName("UTF-8"));
//        try {
//            String json = ExecUtil.execAndGetOutput(commandLine).getStdout();
//            System.out.println(json);
//            Devices result = new Gson().fromJson(json, Devices.class);
//
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//            NotificationUtils.errorNotification( "xcrun invocation failed. Please check that Xcode is installed." );
//            return;
//        }
//        NotificationUtils.infoNotification();

//            String python = Utils.getPythonLocation();
//            if (python == null) {
//                NotificationUtils.pythonNotFound();
//            } else {
//                ReactNativeTerminal.getInstance(currentProject).initAndExecute(new String[]{
//                        "echo", "React Native Console by https://github.com/beansoftapp", getArgs()});
//            }

//        String gradleLocation = RNPathUtil.getAndroidProjectPath(currentProject.getBasePath());
//
//        if (gradleLocation == null) {
//            NotificationUtils.gradleFileNotFound();
//            return;
//        }
//
//        GeneralCommandLine commandLine = new GeneralCommandLine();
////    ExecutionEnvironment environment = getEnvironment();
//        commandLine.setWorkDirectory(gradleLocation);
//        commandLine.setExePath("." + File.separator + "gradlew");
//        commandLine.addParameters("assembleRelease");
//        try {
////            Process process = commandLine.createProcess();
//            OSProcessHandler processHandler = createProcessHandler(commandLine);
//            RunnerUtil.showHelperProcessRunContent("beansoft", processHandler, currentProject, getExecutor());
//            // Run
//            processHandler.startNotify();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//            NotificationUtils.errorNotification("Can't execute command: " + e.getMessage());
//        }
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
