package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;

public class RNUnInstallAction extends BaseRNConsoleNPMAction {
    public RNUnInstallAction(ReactNativeConsole terminal) {
        super(terminal, "react-native uninstall", "react-native uninstall and unlink native dependencies", PluginIcons.Uninstall);
    }

    @Override
    protected String command() {
        return "react-native uninstall";
    }
}
