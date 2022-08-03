package com.github.beansoft.reatnative.idea.utils;

import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.actions.StopProcessAction;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.process.KillableProcessHandler;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.actions.CloseAction;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.terminal.TerminalExecutionConsole;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Show run console for some command or shell command.
 * Some command such as: npx --yes json-format-cli my.json
 * must use the PtyCommandLine:
 * PtyCommandLine commandLine = new PtyCommandLine(Arrays.asList(cmd.split(" ")));
 * Created by beansoft on 17/4/1.
 * @version 2022.8.3
 */
public class RunnerUtil {

    public static final ConsoleView showHelperProcessRunContent(String header, OSProcessHandler runHandler,
                                                                Project project, Executor defaultExecutor, boolean usePtyCommandLine) {
        ProcessTerminatedListener.attach(runHandler);

        ConsoleView consoleView = usePtyCommandLine? new TerminalExecutionConsole(project, runHandler) :  new ConsoleViewImpl(project, true);
        DefaultActionGroup toolbarActions = new DefaultActionGroup();

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(consoleView.getComponent(), "Center");
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("unknown", toolbarActions, false);
        toolbar.setTargetComponent(consoleView.getComponent());
        panel.add(toolbar.getComponent(), "West");

        RunContentDescriptor runDescriptor = new RunContentDescriptor(consoleView,
                runHandler, panel, header, PluginIcons.Application);
        AnAction[]
                consoleActions = consoleView.createConsoleActions();
        toolbarActions.addAll(Arrays.copyOf(consoleActions, consoleActions.length));
        toolbarActions.add(new StopProcessAction("Stop process", "Stop process", runHandler));
        toolbarActions.add(new CloseAction(defaultExecutor, runDescriptor, project));

        consoleView.attachToProcess(runHandler);
//        ExecutionManager.getInstance(environment.getProject()).getContentManager().showRunContent(environment.getExecutor(), runDescriptor);
        showConsole(project, defaultExecutor, runDescriptor);
        return consoleView;
    }

    private static void showConsole(Project project, Executor defaultExecutor, @NotNull RunContentDescriptor contentDescriptor) {
        // Show in run toolwindow
        ExecutionManager.getInstance(project).getContentManager().showRunContent(defaultExecutor, contentDescriptor);
    }

    /**
     * Execute in the runner console window.
     * @param commandLine cmds need to be execute
     * @param project current project
     * @param title tab title
     * @throws ExecutionException
     */
    public static void genInConsole(@NotNull GeneralCommandLine commandLine, @NotNull Project project,
                                     @NotNull String title, boolean usePtyCommandLine) throws
            ExecutionException {
        OSProcessHandler handler = new KillableProcessHandler(commandLine);

        ConsoleView consoleView = showHelperProcessRunContent(title, handler, project, getExecutor(), usePtyCommandLine);
        consoleView.print("cd \"" + commandLine.getWorkDirectory().getAbsolutePath() + "\"\n" ,
                ConsoleViewContentType.SYSTEM_OUTPUT);

        handler.startNotify();// Start the execute
    }

//    /**
//     * Launch process, setup history, actions etc.
//     *
//     * @throws ExecutionException
//     */
//    public void initAndRun() throws ExecutionException {
//        // Create Server process
//        final Process process = createProcess();
//        UIUtil.invokeLaterIfNeeded(() -> {
//            // Init console view
//            myConsoleView = createConsoleView();
//            if (myConsoleView instanceof JComponent) {
//                ((JComponent)myConsoleView).setBorder(new SideBorder(JBColor.border(), SideBorder.LEFT));
//            }
//            myProcessHandler = createProcessHandler(process);
//
//            myConsoleExecuteActionHandler = createExecuteActionHandler();
//
//            ProcessTerminatedListener.attach(myProcessHandler);
//
////            myProcessHandler.addProcessListener(new ProcessAdapter() {
////                @Override
////                public void processTerminated(ProcessEvent event) {
////                    finishConsole();
////                }
////            });
//
//            // Attach to process
//            myConsoleView.attachToProcess(myProcessHandler);
//
//            // Runner creating
//            createContentDescriptorAndActions();
//
//            // Run
//            myProcessHandler.startNotify();
//        });
//    }

    public static Executor getExecutor() {
        return DefaultRunExecutor.getRunExecutorInstance();
    }
}
