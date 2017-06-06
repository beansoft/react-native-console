package com.github.beansoftapp.reatnative.idea.actions;

import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

/**
 * An action to run general commands, doesn't care about directory.
 * Created by beansoft on 2017/5/25.
 */
public abstract class BaseRNConsoleRunAction extends BaseRNConsoleAction {

    public BaseRNConsoleRunAction(ReactNativeConsole terminal, String text) {
        super(terminal, text);
    }

    public BaseRNConsoleRunAction(ReactNativeConsole terminal, String text, String description, Icon icon) {
        super(terminal, text, description, icon);
    }

    @Override
    public void doAction(AnActionEvent anActionEvent) {
        beforeAction();
        terminal.executeShell(command(), null, getText(), getIcon());
        afterAction();
    }

    // Some action before execute commands, eg mkdir through API or shell
    public boolean beforeAction() {
        return true;
    }

    // Some action after execute commands, eg clean dir through API or shell
    public void afterAction() {
    }

    // single line command to run
    protected abstract String command();

}
