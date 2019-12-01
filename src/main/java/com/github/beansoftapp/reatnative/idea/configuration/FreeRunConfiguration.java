package com.github.beansoftapp.reatnative.idea.configuration;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.PtyCommandLine;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.process.KillableProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.terminal.TerminalExecutionConsole;
import com.intellij.util.EnvironmentUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.io.BaseDataReader;
import com.intellij.util.io.BaseOutputReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ReactNative run configuration implementation
 *
 * @author beansoft@126.com
 */
class FreeRunConfiguration extends RunConfigurationBase {

    FreeRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        // setting editor ui
        return new FreeSettingEditor();
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        return new FreeRunState(executionEnvironment);
    }


    /**
     * RunState
     */
    private class FreeRunState implements RunProfileState {
        private ExecutionEnvironment environment;
        FreeRunState(ExecutionEnvironment environment) {
            this.environment = environment;
        }

        @Nullable
        @Override
        public ExecutionResult execute(Executor executor, @NotNull ProgramRunner programRunner) throws ExecutionException {
            return buildExecutionResult();
        }

        private ExecutionResult buildExecutionResult() throws ExecutionException {
            GeneralCommandLine commandLine = createDefaultCommandLine();
            ProcessHandler processHandler = createProcessHandler(commandLine);
            ProcessTerminatedListener.attach(processHandler);
            ConsoleView console = new TerminalExecutionConsole(getProject(), processHandler);

            console.print(
                "Welcome to React Native Console, now please click one button on top toolbar to start.\n",
                ConsoleViewContentType.SYSTEM_OUTPUT);
            console.attachToProcess(processHandler);

            console.print(
                "Give a Star or Suggestion:\n",
                ConsoleViewContentType.NORMAL_OUTPUT);


            processHandler.destroyProcess();
            return new DefaultExecutionResult(console, processHandler);
        }

        @NotNull
        private ProcessHandler createProcessHandler(GeneralCommandLine commandLine) throws ExecutionException {
            return new KillableProcessHandler(commandLine) {
                @NotNull
                @Override
                protected BaseOutputReader.Options readerOptions() {
                    return new BaseOutputReader.Options() {
                        @Override
                        public BaseDataReader.SleepingPolicy policy() {
                            return BaseDataReader.SleepingPolicy.BLOCKING;
                        }

                        @Override
                        public boolean splitToLines() {
                            return false;
                        }

                        @Override
                        public boolean withSeparators() {
                            return true;
                        }
                    };
                }
            };
        }

        protected GeneralCommandLine createDefaultCommandLine() {
            // here just run one command: python freeline.py
            PtyCommandLine commandLine = new PtyCommandLine();
            if (!SystemInfo.isWindows) {
                commandLine.getEnvironment().put("TERM", "xterm-256color");
            }
//            commandLine.withConsoleMode(false);
//            commandLine.withInitialColumns(120);
            ExecutionEnvironment environment = getEnvironment();
            commandLine.setWorkDirectory(environment.getProject().getBasePath());
            String defaultShell = ObjectUtils.notNull(EnvironmentUtil.getValue("SHELL"), "/bin/sh");
            commandLine.setExePath(defaultShell);
//            commandLine.setExePath("npm");
//            commandLine.addParameters("run-script");
//            commandLine.addParameters("start");
            return commandLine;
        }

        public ExecutionEnvironment getEnvironment() {
            return environment;
        }

        public void setEnvironment(ExecutionEnvironment environment) {
            this.environment = environment;
        }


    }
}
