package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleAndroidAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.OSUtils;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

import java.io.File;

public class AndroidCleanAction extends BaseRNConsoleAndroidAction {
    public AndroidCleanAction(ReactNativeConsole terminal) {
        super(terminal, "Android Clean APK", "gradlew clean", PluginIcons.RESET_TO_EMPTY);
    }

    protected String command() {
        if (OSUtils.isWindows()) {// https://github.com/beansoftapp/react-native-console/issues/8
            return "gradlew.bat clean";
        }
        return "." + File.separator + "gradlew clean";
    }
}
