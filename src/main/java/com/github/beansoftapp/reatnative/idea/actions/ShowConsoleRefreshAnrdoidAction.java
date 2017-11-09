package com.github.beansoftapp.reatnative.idea.actions;

import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.icons.AllIcons;

import javax.swing.*;

/**
 * Click to display the React Native Console window and run refresh android action.
 * Created by beansoft on 2017/11/08.
 */
public class ShowConsoleRefreshAnrdoidAction extends BaseAction {

    public ShowConsoleRefreshAnrdoidAction() {
        super(AllIcons.Actions.Refresh);
    }

    public ShowConsoleRefreshAnrdoidAction(Icon icon) {
        super(icon);
    }

    @Override
    public void actionPerformed() {
        ReactNativeConsole.getInstance(currentProject).initAndActiveRunRefresh(anActionEvent.getInputEvent());
    }

}