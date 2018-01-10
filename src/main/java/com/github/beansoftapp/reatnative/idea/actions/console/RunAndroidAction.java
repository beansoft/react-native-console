package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

public class RunAndroidAction extends BaseRNConsoleNPMAction {
    public RunAndroidAction(ReactNativeConsole terminal) {
        super(terminal, "Debug Android", "builds your app and starts it on a connected Android emulator or device", PluginIcons.Android);
    }

    protected String command() {
        return "react-native run-android";
    }
}
