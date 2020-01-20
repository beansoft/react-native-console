package com.github.beansoftapp.reatnative.idea.utils.rn;

import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.utils.npm.NPMParser;
import com.intellij.openapi.project.Project;

import java.io.File;

public class RNVersionSwitcher {
  /**
   * If RN 0.60+, using yarn react-native.
   * @param command
   * @param project
   * @return
   */
  public static final String rnVersionCommand(String command, Project project) {
    if(command != null && command.startsWith("react-native")) {
      String version = NPMParser.parseRNVersion(new File(RNPathUtil.getRNProjectPath(project), "package.json" ) );
      if(version != null && version.compareTo("0.60") >= 0) {
        command = command.replaceFirst("react-native", "yarn react-native");
      }
    }


    return command;
  }
}
