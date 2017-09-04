package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

// view android log
public class NPMAndroidLogsAction extends BaseRNConsoleNPMAction {
    public NPMAndroidLogsAction(ReactNativeConsole terminal) {
        super(terminal, "log-android", "react-native log-android", PluginIcons.TrackTests);
    }

    protected String command() {
        return "adb logcat *:S ReactNative:V ReactNativeJS:V";
    }
}
