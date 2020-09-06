package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoft.reatnative.idea.entity.ProjectConfig;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.utils.RNPathUtil;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.util.text.StringUtil;

public class RunAndroidAction extends BaseRNConsoleNPMAction {
    public RunAndroidAction(ReactNativeConsole terminal) {
        super(terminal, "Debug Android", "builds your app and starts it on a connected Android emulator or device", PluginIcons.Android);
    }

    protected String command() {
        ProjectConfig projectConfig = RNPathUtil.parseConfigFromRNConsoleJsonFile(super.getProject());

        String param = projectConfig.getAndroidParam();

        if (StringUtil.isEmpty(param)) {
            param = "";
        } else {
            param = " " + param;
        }

        return "react-native run-android" + getMetroPortParams() + param;
    }
}
