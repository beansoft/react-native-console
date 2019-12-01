// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.github.beansoftapp.reatnative.idea.sh;

import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.SimpleConfigurationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NotNullLazyValue;

import com.intellij.util.EnvironmentUtil;
import org.jetbrains.annotations.NotNull;

public class ShConfigurationType extends SimpleConfigurationType {
  public static final String ID = "RNConsole";
  public ShConfigurationType() {
    super("ShConfigurationType", ID, ID + " configuration",
          NotNullLazyValue.createValue(() -> PluginIcons.React));
  }

  @NotNull
  @Override
  public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
    ShRunConfiguration configuration = new ShRunConfiguration(project, this, ID);
    String defaultShell = EnvironmentUtil.getValue("SHELL");
    if (defaultShell != null) {
      configuration.setInterpreterPath(defaultShell);
    }
    String projectPath = project.getBasePath();
    if (projectPath != null) {
      configuration.setScriptWorkingDirectory(projectPath);
    }
    return configuration;
  }
}
