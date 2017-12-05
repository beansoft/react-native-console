package com.github.beansoftapp.reatnative.idea.actions;

import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

/**
 * An action to run general npm commands.
 * Created by beansoft on 2017/5/26.
 */
public abstract class BaseRNConsoleNPMAction extends BaseRNConsoleRunAction {

    public BaseRNConsoleNPMAction(ReactNativeConsole terminal, String text) {
        super(terminal, text);
    }

    public BaseRNConsoleNPMAction(ReactNativeConsole terminal, String text, String description, Icon icon) {
        super(terminal, text, description, icon);
    }

    @Override
    public void doAction(AnActionEvent anActionEvent) {
        if(beforeAction()) {
            terminal.runNPMCI(command(), getText(), getIcon());
            afterAction();
        }
    }
}