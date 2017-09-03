package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleAndroidAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.OSUtils;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

import java.io.File;

public class AndroidDebugApkAction extends BaseRNConsoleAndroidAction {
    public AndroidDebugApkAction(ReactNativeConsole terminal) {
        super(terminal, "Debug APK", "Generate Debug APK file", PluginIcons.StartDebugger);
    }

    protected String command() {
        if (OSUtils.isWindows()) {// https://github.com/beansoftapp/react-native-console/issues/8
            return "gradlew.bat assembleDebug";
        }
        return "." + File.separator + "gradlew assembleDebug";
    }
}
