// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.github.beansoftapp.reatnative.idea.sh;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.terminal.JBTerminalWidget;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.jediterm.terminal.ProcessTtyConnector;
import com.pty4j.PtyProcess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner;
import org.jetbrains.plugins.terminal.TerminalToolWindowFactory;
import org.jetbrains.plugins.terminal.TerminalUtil;
import org.jetbrains.plugins.terminal.TerminalView;
import org.jetbrains.plugins.terminal.arrangement.TerminalWorkingDirectoryManager;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class ShTerminalRunner extends ShRunner {
  private static final Logger LOG = Logger.getInstance(LocalTerminalDirectRunner.class);

  public ShTerminalRunner(@NotNull Project project) {
    super(project);
  }

  @Override
  public void run(@NotNull String command, @NotNull String workingDirectory) {
    TerminalView terminalView = TerminalView.getInstance(myProject);
    ToolWindow window = ToolWindowManager.getInstance(myProject).getToolWindow(TerminalToolWindowFactory.TOOL_WINDOW_ID);
    if (window == null) return;

    ContentManager contentManager = window.getContentManager();
    Pair<Content, Process> pair = getSuitableProcess(contentManager, workingDirectory);
    if (pair != null) {
      try {
        window.activate(null);
        contentManager.setSelectedContent(pair.first);
        runCommand(pair.second, command);
      }
      catch (ExecutionException e) {
        LOG.warn("Error running terminal", e);
      }
    }
    else {
      terminalView.createNewSession(new LocalTerminalDirectRunner(myProject) {
        @Override
        protected PtyProcess createProcess(@Nullable String directory, @Nullable String commandHistoryFilePath) throws ExecutionException {
          PtyProcess process = super.createProcess(workingDirectory, commandHistoryFilePath);
          runCommand(process, command);
          return process;
        }
      });
    }
  }

  @Override
  public boolean isAvailable(@NotNull Project project) {
    ToolWindow window = ToolWindowManager.getInstance(project).getToolWindow(TerminalToolWindowFactory.TOOL_WINDOW_ID);
    return window != null && window.isAvailable();
  }

  @Nullable
  private static Pair<Content, Process> getSuitableProcess(@NotNull ContentManager contentManager, @NotNull String workingDirectory) {
    Content selectedContent = contentManager.getSelectedContent();
    if (selectedContent != null) {
      Pair<Content, Process> pair = getSuitableProcess(selectedContent, workingDirectory);
      if (pair != null) return pair;
    }

    return Arrays.stream(contentManager.getContents())
      .map(content -> getSuitableProcess(content, workingDirectory))
      .filter(Objects::nonNull)
      .findFirst()
      .orElse(null);
  }

  @Nullable
  private static Pair<Content, Process> getSuitableProcess(@NotNull Content content, @NotNull String workingDirectory) {
    JBTerminalWidget widget = TerminalView.getWidgetByContent(content);
    if (widget == null) return null;
    if (widget.getTtyConnector() instanceof ProcessTtyConnector) {
      ProcessTtyConnector ttyConnector = (ProcessTtyConnector)widget.getTtyConnector();
      String currentWorkingDirectory = TerminalWorkingDirectoryManager.getWorkingDirectory(widget, null);
      if (currentWorkingDirectory == null) return null;
      if (!TerminalUtil.hasRunningCommands(ttyConnector) && currentWorkingDirectory.equals(workingDirectory)) {
        return Pair.create(content, getProcessFromProcessTtyConnector(ttyConnector)); //ttyConnector.getProcess() 2019
      }
    }
    return null;
  }

  // for 2017 to 2019
  private static Process getProcessFromProcessTtyConnector(ProcessTtyConnector ttyConnector) {
    try {
      Method method = ProcessTtyConnector.class.getMethod("ttyConnector");
      if(method != null) {
        return (Process) method.invoke(ttyConnector);
      }
    } catch (Exception e) {
      e.printStackTrace();
      try {
        Field field = ProcessTtyConnector.class.getField("myProcess");
        if(field != null) {
          return (Process) field.get(ttyConnector);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    return null;
  }

  private static void runCommand(@NotNull Process process, @Nullable String command)
    throws ExecutionException {
    if (command != null) {
      try {
        // Workaround for ANSI escape code IDEA-221031
        process.getOutputStream().write(KeyEvent.VK_BACK_SPACE);
        process.getOutputStream().write(command.getBytes(CharsetToolkit.UTF8_CHARSET));
      }
      catch (IOException ex) {
        throw new ExecutionException("Fail to start " + command, ex);
      }
    }
    else {
      throw new ExecutionException("Cannot run command:" + command, null);
    }
  }
}
