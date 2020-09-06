package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;

public class RNLinkAction extends BaseRNConsoleNPMAction {
    public RNLinkAction(ReactNativeConsole terminal) {
        super(terminal, "react-native link", "react-native link links all native dependencies (updates native build files)", PluginIcons.Lightning);
    }

    protected String command() {
        return "react-native link";
    }
}
