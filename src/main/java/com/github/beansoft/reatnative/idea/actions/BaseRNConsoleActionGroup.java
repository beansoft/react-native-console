package com.github.beansoft.reatnative.idea.actions;

import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/** Base action group, allow execute during an index */
public class BaseRNConsoleActionGroup extends ActionGroup {
    protected ReactNativeConsole terminal;
    public BaseRNConsoleActionGroup(ReactNativeConsole terminal, String text, String description, Icon icon) {
        super(text, description, icon);
        this.terminal = terminal;
    }

    @Override
    public boolean isDumbAware() {
        return true;
    }

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent e) {
        return EMPTY_ARRAY;
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }
}