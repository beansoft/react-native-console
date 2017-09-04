package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

public class RunAndroidAction extends BaseRNConsoleNPMAction {
    public RunAndroidAction(ReactNativeConsole terminal) {
        super(terminal, "Debug Android", "react-native run-android", PluginIcons.Android);
    }

    protected String command() {
        return "react-native run-android";
    }
}
