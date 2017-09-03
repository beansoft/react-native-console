package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

public class RNLinkAction extends BaseRNConsoleNPMAction {
    public RNLinkAction(ReactNativeConsole terminal) {
        super(terminal, "RN link", "react-native link", PluginIcons.Lightning);
    }

    protected String command() {
        return "react-native link";
    }
}
