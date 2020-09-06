package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleAndroidAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.utils.OSUtils;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;

import java.io.File;

public class AndroidReleaseApkAction extends BaseRNConsoleAndroidAction {
    public AndroidReleaseApkAction(ReactNativeConsole terminal) {
        super(terminal, "Release APK", "Generate Release APK file", PluginIcons.Archive);
    }

    protected String command() {
        if (OSUtils.isWindows()) {// https://github.com/beansoftapp/react-native-console/issues/8
            return "gradlew.bat assembleRelease";
        }
        return "." + File.separator + "gradlew assembleRelease";
    }
}
