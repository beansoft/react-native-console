package com.github.beansoftapp.reatnative.idea.views;

import com.github.beansoftapp.reatnative.idea.ui.RNConsole;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.actions.StopProcessAction;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.impl.ConsoleState;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.icons.AllIcons;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

/**
 * A React Native Console, which can reuse console and execute commands. With rerun supports.
 * Created by beansoft on 2017/5/25.
 */
public class RNConsoleImpl extends ConsoleViewImpl implements RNConsole {

    // Rerun current command
    private class RerunAction extends AnAction {
        public RerunAction() {
            super("Rerun", "Rerun",
                    AllIcons.Actions.Restart);
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            reRun();
        }

        @Override
        public void update(AnActionEvent e) {
            e.getPresentation().setVisible(myGeneralCommandLine != null);
            e.getPresentation().setEnabled(myGeneralCommandLine != null);
            if(displayName != null) {
                e.getPresentation().setText("Rerun '" + displayName + "'");
                e.getPresentation().setDescription("Rerun '" + displayName + "'");
            } else if(myGeneralCommandLine != null) {
                e.getPresentation().setText("Rerun '" + myGeneralCommandLine.getCommandLineString() + "'");
                e.getPresentation().setDescription("Rerun '" + myGeneralCommandLine.getCommandLineString() + "'");
            }
        }

        @Override
        public boolean isDumbAware() {
//            return Registry.is("dumb.aware.run.configurations");
            return true;
        }
    }

    private ProcessHandler myProcessHandler;
    private GeneralCommandLine myGeneralCommandLine;

    private StopProcessAction myStopProcessAction;

    private String displayName;// Friendly display name

    public RNConsoleImpl(@NotNull Project project, boolean viewer) {
        super(project, viewer);
    }

    public RNConsoleImpl(@NotNull Project project, @NotNull GlobalSearchScope searchScope, boolean viewer, boolean usePredefinedMessageFilter) {
        super(project, searchScope, viewer, usePredefinedMessageFilter);
    }

    protected RNConsoleImpl(@NotNull Project project, @NotNull GlobalSearchScope searchScope, boolean viewer, @NotNull ConsoleState initialState, boolean usePredefinedMessageFilter) {
        super(project, searchScope, viewer, initialState, usePredefinedMessageFilter);
    }

    public AnAction getReRunAction() {
        return new RerunAction();
    }

    public void setStopProcessAction(StopProcessAction myStopProcessAction) {
        this.myStopProcessAction = myStopProcessAction;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void reRun() {
        if(myGeneralCommandLine != null) {
            try {
                processCommandline(myGeneralCommandLine);
            } catch (ExecutionException e) {
                NotificationUtils.showNotification("Unable to run the commandline:" + e.getMessage(),
                        NotificationType.WARNING);
            }
        }
    }

    /**
     * Execute some shell with giving working directory.
     *
     * @param shell
     */
    public void executeShell(String shell, String workDirectory) {
        GeneralCommandLine commandLine =RNPathUtil.createFullPathCommandLine(shell, workDirectory);
        commandLine.setWorkDirectory(workDirectory);
        myGeneralCommandLine = commandLine;
        try {
            processCommandline(commandLine);
        } catch (ExecutionException e) {
            NotificationUtils.showNotification("Unable to run the commandline:" + e.getMessage(),
                    NotificationType.WARNING);
        }
    }

    /**
     * Execute raw commands without any path or param modify.
     *
     * @param shell
     */
    public void executeRawShell(String workDirectory, String[] shell) {
        GeneralCommandLine commandLine =new GeneralCommandLine(shell);
        commandLine.setWorkDirectory(workDirectory);
        myGeneralCommandLine = commandLine;
        try {
            processCommandline(commandLine);
        } catch (ExecutionException e) {
            NotificationUtils.showNotification("Unable to run the commandline:" + e.getMessage(),
                    NotificationType.WARNING);
        }
    }

    /**
     * run gradle commands in android project dir
     * @param command
     */
    public void runGradleCI(String command) {
        String path = RNPathUtil.getRNProjectPath(getProject());
        String gradleLocation = RNPathUtil.getAndroidProjectPath(path);
        if (gradleLocation == null) {
            NotificationUtils.gradleFileNotFound();
        } else {
            executeShell(command, gradleLocation);
        }
    }

    /**
     * run npm commands in package.json project dir
     * @param command
     */
    public void runNPMCI(String command) {
        String npmLocation = RNPathUtil.getRNProjectPath(getProject());

        if (npmLocation == null) {
            NotificationUtils.packageJsonNotFound();
        } else {
            executeShell(command, npmLocation);
        }
    }

    /**
     * run npm commands in package.json project dir, execute raw commands without any path or param modify.
     * @param command
     */
    public void runRawNPMCI(String... command) {
        String npmLocation = RNPathUtil.getRNProjectPath(getProject());

        if (npmLocation == null) {
            NotificationUtils.packageJsonNotFound();
        } else {
            executeRawShell(npmLocation, command);
        }
    }

    /* process command line, with very simple console view and tool window */
    private void processCommandline(GeneralCommandLine commandLine) throws ExecutionException {
        if(myProcessHandler != null) {
            myProcessHandler.destroyProcess();
//            ExecutionManagerImpl.stopProcess(myProcessHandler); // New Android Studio doesn't have this method anymore
            clear();
            myProcessHandler = null;
        }

        if(commandLine.getWorkDirectory() != null) {
            print(
                "cd \"" + commandLine.getWorkDirectory().getAbsolutePath() + "\"\n" ,
                ConsoleViewContentType.SYSTEM_OUTPUT);
        }


        final OSProcessHandler processHandler = new OSProcessHandler(commandLine);
        myProcessHandler = processHandler;
        myStopProcessAction.setProcessHandler(processHandler);

        ProcessTerminatedListener.attach(processHandler);

        processConsole(processHandler);

//        ApplicationManager.getApplication().invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                processConsole(processHandler);
//            }
//        });
    }

    /* process attach to console, show the shell execution log */
    // TODO: 2016/9/14 0014 need refactor console method
    private void processConsole(ProcessHandler processHandler) {
        attachToProcess(processHandler);
        processHandler.startNotify();// If not call this, the command content will not be shown
    }

    /**
     * Clean up process when close tab
     * @since 1.0.6
     */
    public void dispose() {
        super.dispose();
        if(myProcessHandler != null && !myProcessHandler.isProcessTerminated() ) {
            System.out.println("Terminate process of tab " + displayName + ", cmd:" + myGeneralCommandLine);
//            ExecutionManagerImpl.stopProcess(myProcessHandler);
            myProcessHandler.destroyProcess();
            myProcessHandler = null;
        }
    }

}
