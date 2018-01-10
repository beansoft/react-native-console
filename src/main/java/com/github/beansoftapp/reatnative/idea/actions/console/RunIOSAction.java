package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

public class RunIOSAction extends BaseRNConsoleNPMAction {
    public RunIOSAction(ReactNativeConsole terminal) {
        super(terminal, "iOS Run Simulator", "builds your app and starts it on iOS simulator", PluginIcons.IPhoneSimulator);
    }

    protected String command() {
        return "react-native run-ios";
    }
}
