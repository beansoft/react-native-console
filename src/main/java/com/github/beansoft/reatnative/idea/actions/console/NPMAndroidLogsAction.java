package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;

// view android log
public class NPMAndroidLogsAction extends BaseRNConsoleNPMAction {
    public NPMAndroidLogsAction(ReactNativeConsole terminal) {
        super(terminal, "log-android", "react-native log-android starts adb logcat", PluginIcons.TrackTests);
    }

    protected String command() {
        return "adb logcat *:S ReactNative:V ReactNativeJS:V";
    }
}
