package com.github.beansoftapp.reatnative.idea.actions;

import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.util.ExecUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

/**
 * Click to display the React Native Console window and run refresh android action.
 * @version 2018.1 headless mode, no more main active RN console window
 * Created by beansoft on 2017/11/08.
 */
public class ShowConsoleRefreshAnrdoidAction extends AnAction {

    public ShowConsoleRefreshAnrdoidAction() {
        super(AllIcons.Actions.Refresh);
    }

    public ShowConsoleRefreshAnrdoidAction(Icon icon) {
        super(icon);
    }

    // Always available and enabled
    @Override
    public boolean isDumbAware() {
        return true;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
//        ReactNativeConsole.getInstance(currentProject).initAndActiveRunRefresh(anActionEvent.getInputEvent());
        GeneralCommandLine commandLine = RNPathUtil.createFullPathCommandLine(command(), null);
        try {
            ExecUtil.execAndGetOutput(commandLine);
            NotificationUtils.infoNotification("Android Reload JS done.");
        } catch (ExecutionException e) {
            e.printStackTrace();
            NotificationUtils.errorNotification( "Android Reload JS failed. Please check that adb is installed." );
        }
    }

    protected String command() {
        //return "adb shell input keyevent 82 20 66 66";//First toggle menu, then press down key to select first menu item
        // - Reload, final press enter will execute the action

        // For latest RN 0.59+, using this Double tap R on your keyboard to reload your app's code.
        return "adb shell input keyevent 46 46";
    }
}
