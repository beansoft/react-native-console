package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.entity.ProjectConfig;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.util.text.StringUtil;

public class RunIOSAction extends BaseRNConsoleNPMAction {
    public RunIOSAction(ReactNativeConsole terminal) {
        super(terminal, "iOS Run Simulator", "builds your app and starts it on iOS simulator", PluginIcons.IPhoneSimulator);
    }

    protected String command() {
        ProjectConfig projectConfig = RNPathUtil.parseConfigFromRNConsoleJsonFile(super.getProject());

        String param = projectConfig.getIosParam();

        if (StringUtil.isEmpty(param)) {
            param = "";
        } else {
            param = " " + param;
        }

        return "react-native run-ios" + getMetroPortParams() + param;
    }
}
