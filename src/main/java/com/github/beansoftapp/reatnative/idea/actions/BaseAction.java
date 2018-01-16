package com.github.beansoftapp.reatnative.idea.actions;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.io.File;

/**
 * Base action with DumbAware enabled, allow to run during index.
 * @author beansoft
 * @date 2018-01-16
 */
public abstract class BaseAction extends AnAction {

    protected Project currentProject;
    protected File projectDir;
    protected AnActionEvent anActionEvent;

    public BaseAction(Icon icon) {
        super(icon);
    }

    @Override
    public boolean isDumbAware() {
        return true;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
//        DocumentUtil.saveDocument();
        this.anActionEvent = anActionEvent;
        this.currentProject = PlatformDataKeys.PROJECT.getData(anActionEvent.getDataContext());
        this.projectDir = new File(currentProject.getBasePath());
        actionPerformed();
    }

    public abstract void actionPerformed();

    protected OSProcessHandler createProcessHandler( GeneralCommandLine commandLine) throws ExecutionException {
        return new KillableColoredProcessHandler( commandLine);
    }

    protected Executor getExecutor() {
        return DefaultRunExecutor.getRunExecutorInstance();
    }

    /**
     * 异步执行
     *
     * @param runnable
     */
    protected void asyncTask(Runnable runnable) {
        ApplicationManager.getApplication().executeOnPooledThread(runnable);
    }

    protected void invokeLater(Runnable runnable) {
        ApplicationManager.getApplication().invokeLater(runnable);
    }

}
