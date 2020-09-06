package com.github.beansoft.reatnative.idea.ui;

import com.intellij.execution.actions.StopProcessAction;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.AnAction;

public interface RNConsole extends ConsoleView {
  void setStopProcessAction(StopProcessAction myStopProcessAction);

  AnAction getReRunAction();

  /**
   * run gradle commands in android project dir
   * @param command
   */
  void runGradleCI(String command);

  /**
   * run npm commands in package.json project dir
   * @param command
   */
  void runNPMCI(String command);

  void runCocoapods(String command);

  void setDisplayName(String displayName);

  /**
   * Execute some shell with giving working directory.
   *
   * @param shell
   */
  void executeShell(String shell, String workDirectory);

  /**
   * Execute raw commands without any path or param modify.
   *
   * @param shell
   */
  void executeRawShell(String workDirectory, String[] shell);

  /**
   * run npm commands in package.json project dir, execute raw commands without any path or param modify.
   * @param command
   */
  void runRawNPMCI(String... command);
}
