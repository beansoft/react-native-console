package com.github.beansoft.reatnative.idea.utils.rn;

import com.github.beansoft.reatnative.idea.utils.RNPathUtil;
import com.github.beansoft.reatnative.idea.utils.npm.NPMParser;
import com.intellij.openapi.project.Project;

import java.io.File;

public class RNVersionSwitcher {
  /**
   * If RN 0.60+, using npx react-native.
   * @param command
   * @param project
   * @return
   */
  public static final String rnVersionCommand(String command, Project project) {
    if(command != null && command.startsWith("react-native")) {
      String version = NPMParser.parseRNVersion(new File(RNPathUtil.getRNProjectPath(project), "package.json" ) );
      if(version != null && version.compareTo("0.60") >= 0) {
        String preferredCommand = "npx";

        command = command.replaceFirst("react-native", preferredCommand + " react-native");
      }
    }


    return command;
  }
}
