package com.github.beansoftapp.reatnative.idea.actions;

import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionManagerImpl;
import com.intellij.openapi.actionSystem.impl.MenuItemPresentationFactory;
import com.intellij.openapi.wm.impl.content.ToolWindowContentUi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

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

        new GearAction().actionPerformed(anActionEvent);
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
//            Simulators result = new Gson().fromJson(json, Simulators.class);
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

    private void showGearPopup(Component component, int x, int y) {
        ActionPopupMenu popupMenu =
                ((ActionManagerImpl) ActionManager.getInstance())
                        .createActionPopupMenu(ToolWindowContentUi.POPUP_PLACE, createGearPopupGroup(), new MenuItemPresentationFactory(true));
        popupMenu.getComponent().show(component, x, y);
    }

    private DefaultActionGroup createGearPopupGroup() {
        DefaultActionGroup group = new DefaultActionGroup();

        group.add(new AnAction("Test") {

            @Override
            public void actionPerformed(AnActionEvent anActionEvent) {
                NotificationUtils.infoNotification("Test");
            }
        });
        group.addSeparator();

        return group;
    }

    private class GearAction extends AnAction {
        GearAction() {
            Presentation presentation = getTemplatePresentation();
            presentation.setIcon(AllIcons.General.Gear);
            presentation.setHoveredIcon(AllIcons.General.GearHover);
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            int x = 0;
            int y = 0;
            InputEvent inputEvent = e.getInputEvent();
            if (inputEvent instanceof MouseEvent) {
                x = ((MouseEvent)inputEvent).getX();
                y = ((MouseEvent)inputEvent).getY();
            }

            showGearPopup(inputEvent.getComponent(), x, y);
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
