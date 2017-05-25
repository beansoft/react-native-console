package com.github.beansoftapp.reatnative.idea.views;

import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.actions.StopProcessAction;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.impl.ConsoleState;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.impl.ExecutionManagerImpl;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

/**
 * A React Native Console, which can reuse window and execute commands.
 * Created by beansoft on 2017/5/25.
 */
public class RNConsoleImpl extends ConsoleViewImpl {

    protected ProcessHandler myProcessHandler;

    public void setStopProcessAction(StopProcessAction myStopProcessAction) {
        this.myStopProcessAction = myStopProcessAction;
    }

    protected StopProcessAction myStopProcessAction;

    public RNConsoleImpl(@NotNull Project project, boolean viewer) {
        super(project, viewer);
    }

    public RNConsoleImpl(@NotNull Project project, @NotNull GlobalSearchScope searchScope, boolean viewer, boolean usePredefinedMessageFilter) {
        super(project, searchScope, viewer, usePredefinedMessageFilter);
    }

    protected RNConsoleImpl(@NotNull Project project, @NotNull GlobalSearchScope searchScope, boolean viewer, @NotNull ConsoleState initialState, boolean usePredefinedMessageFilter) {
        super(project, searchScope, viewer, initialState, usePredefinedMessageFilter);
    }

    /**
     * 执行shell
     * 利用terminal换行即执行原理
     *
     * @param shell
     */
    public void executeShell(String shell, String workDirectory) {
        GeneralCommandLine commandLine = new GeneralCommandLine(shell.split(" "));
        commandLine.setWorkDirectory(workDirectory);
        try {
            processCommandline(getProject(), commandLine);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void runGradleCI(String command) {
        String path = getProject().getBasePath();
        String gradleLocation = RNPathUtil.getAndroidProjectPath(path);
        if (gradleLocation == null) {
            NotificationUtils.gradleFileNotFound();
        } else {
            executeShell(command, gradleLocation);
        }
    }

    public void runNPMCI(String command) {
        String path = getProject().getBasePath();
        String npmLocation = RNPathUtil.getRNProjectPath(getProject(), path);

        if (npmLocation == null) {
            NotificationUtils.packageJsonNotFound();
        } else {
            executeShell(command, npmLocation);
        }
    }

    /* process command line, will very simple console view and tool window */
    private void processCommandline(final Project project, GeneralCommandLine commandLine) throws ExecutionException {
        if(myProcessHandler != null) {
            ExecutionManagerImpl.stopProcess(myProcessHandler);
            myProcessHandler = null;
        }

        final OSProcessHandler processHandler = new OSProcessHandler(commandLine);
        myProcessHandler = processHandler;
        myStopProcessAction.setProcessHandler(processHandler);

        ProcessTerminatedListener.attach(processHandler);

        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                processConsole(processHandler);
            }
        });
    }

    /* process attach to console,show the log */
    // TODO: 2016/9/14 0014 need refactor console method
    private void processConsole(ProcessHandler processHandler) {
        clear();
        attachToProcess(processHandler);
        processHandler.startNotify();// Don't call this, the command content will not be shown
    }

}
