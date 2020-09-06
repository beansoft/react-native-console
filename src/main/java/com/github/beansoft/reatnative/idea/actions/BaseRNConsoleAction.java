package com.github.beansoft.reatnative.idea.actions;

import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;

import javax.swing.*;

/**
 * Abstract RN console action.
 * Created by beansoft on 2017/5/25.
 */
public abstract class BaseRNConsoleAction extends DumbAwareAction {
    protected ReactNativeConsole terminal;
    protected Project project;
    protected DataContext dataContext;

    public BaseRNConsoleAction(ReactNativeConsole terminal, String text, String description, Icon icon) {
        super(text, description, icon);
        this.terminal = terminal;
    }

    public BaseRNConsoleAction(ReactNativeConsole terminal, String text) {
        super(text);
        this.terminal = terminal;
    }

    public String getText() {
        return getTemplatePresentation().getText();
    }

    public Icon getIcon() {
        return getTemplatePresentation().getIcon();
    }

    public Project getProject() {
        return project;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
//            DocumentUtil.saveDocument();
        dataContext = e.getDataContext();
        project = e.getProject();

        doAction(e);
    }

    @Override
    public boolean isDumbAware() {
        return true;
    }

    public abstract void doAction(AnActionEvent anActionEvent);
}