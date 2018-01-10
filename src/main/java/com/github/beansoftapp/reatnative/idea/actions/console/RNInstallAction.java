package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

public class RNInstallAction extends BaseRNConsoleNPMAction {
    public RNInstallAction(ReactNativeConsole terminal) {
        super(terminal, "react-native install", "react-native install and link native dependencies", PluginIcons.Install);
    }

    @Override
    protected String command() {
        return "react-native install";
    }
}
