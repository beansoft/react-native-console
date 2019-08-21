package com.github.beansoftapp.reatnative.idea.actions.console.hyperlink;

import com.github.beansoftapp.reatnative.idea.entity.ProjectConfig;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.IdeaMessages;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.intellij.execution.filters.HyperlinkInfoBase;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditRunAnroidOptions extends HyperlinkInfoBase {
  @Override
  public void navigate(@NotNull Project project, @Nullable RelativePoint relativePoint) {
    ProjectConfig projectConfig = RNPathUtil.parseConfigFromRNConsoleJsonFile(project);
    String value = projectConfig.getAndroidParam();
    if(StringUtil.isEmpty(value)) {
      value = "<empty>";
    }

    String androidConfig = IdeaMessages.showInputDialog(project,
        "Current options are " + value +
            ".\nEdit run-android command options, eg: --appIdSuffix debug. Input empty value to disable it.\nThe value is stored in file .idea/.rnconsole",
        "Edit Run-Android Command Options",
        PluginIcons.Android,
        projectConfig.getAndroidParam(),
        null);

    projectConfig.setAndroidParam(androidConfig);

    RNPathUtil.saveProjectConfig(project, projectConfig);
  }
}
