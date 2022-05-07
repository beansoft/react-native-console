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
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ui.ExecutionConsole;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.actions.CloseAction;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Created by beansoft on 17/4/1.
 */
@Deprecated
public class RunnerUtil {

    public static final ConsoleView showHelperProcessRunContent(String header, OSProcessHandler runHandler, Project project, Executor defaultExecutor) {
        ProcessTerminatedListener.attach(runHandler);

        ConsoleViewImpl consoleView = new ConsoleViewImpl(project, true);
        DefaultActionGroup toolbarActions = new DefaultActionGroup();

        JPanel panel = new JPanel((LayoutManager) new BorderLayout());
        panel.add((Component) consoleView.getComponent(), "Center");
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("unknown", (ActionGroup) toolbarActions, false);
        toolbar.setTargetComponent(consoleView.getComponent());
        panel.add((Component) toolbar.getComponent(), "West");

        RunContentDescriptor runDescriptor = new RunContentDescriptor((ExecutionConsole) consoleView,
                (ProcessHandler) runHandler, (JComponent) panel, header, PluginIcons.Application);
        AnAction[]
                consoleActions = consoleView.createConsoleActions();
        toolbarActions.addAll((AnAction[]) Arrays.copyOf(consoleActions, consoleActions.length));
        toolbarActions.add((AnAction) new StopProcessAction("Stop process", "Stop process", (ProcessHandler) runHandler));
        toolbarActions.add((AnAction) new CloseAction(defaultExecutor, runDescriptor, project));

        consoleView.attachToProcess((ProcessHandler) runHandler);
//        ExecutionManager.getInstance(environment.getProject()).getContentManager().showRunContent(environment.getExecutor(), runDescriptor);
        showConsole(project, defaultExecutor, runDescriptor);
        return (ConsoleView) consoleView;
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
                                     @NotNull String title) throws
            ExecutionException {
        OSProcessHandler handler = new KillableProcessHandler(commandLine);

        ConsoleView consoleView = showHelperProcessRunContent(title, handler, project, getExecutor());
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
