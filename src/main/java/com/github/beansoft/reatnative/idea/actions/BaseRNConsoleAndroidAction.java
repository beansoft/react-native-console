package com.github.beansoft.reatnative.idea.actions;

import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

/**
 * An action to run general gradle commands.
 * Created by beansoft on 2017/5/26.
 */
public abstract class BaseRNConsoleAndroidAction extends BaseRNConsoleRunAction {

    public BaseRNConsoleAndroidAction(ReactNativeConsole terminal, String text) {
        super(terminal, text);
    }

    public BaseRNConsoleAndroidAction(ReactNativeConsole terminal, String text, String description, Icon icon) {
        super(terminal, text, description, icon);
    }

    @Override
    public void doAction(AnActionEvent anActionEvent) {
        beforeAction();
        terminal.runGradleCI(command(), getText(), getIcon());
        afterAction();
    }
}