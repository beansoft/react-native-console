package com.github.beansoftapp.reatnative.idea.actions;

import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.utils.npm.NPMParser;
import com.github.beansoftapp.reatnative.idea.views.RNConsoleImpl;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

/** Base action group */
public class BaseRNConsoleActionGroup extends ActionGroup {
    protected ReactNativeConsole terminal;
    public BaseRNConsoleActionGroup(ReactNativeConsole terminal, String text, String description, Icon icon) {
        super(text, description, icon);
        this.terminal = terminal;
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