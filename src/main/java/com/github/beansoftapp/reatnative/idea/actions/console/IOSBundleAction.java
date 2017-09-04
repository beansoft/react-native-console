package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

import java.io.File;

public class IOSBundleAction extends BaseRNConsoleNPMAction {
    public IOSBundleAction(ReactNativeConsole terminal) {
        super(terminal, "iOS RN Bundle",
                "Create Release React Native Bundle File for iOS", PluginIcons.Deploy);
    }

    public boolean beforeAction() {
        String npmLocation = RNPathUtil.getRNProjectPath(getProject());

        if (npmLocation == null) {
            NotificationUtils.packageJsonNotFound();
        } else {
            try {
                File dir = new File(npmLocation, "ios/bundle");
                if (!dir.exists())
                    dir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    protected String command() {
        return "react-native bundle --platform ios --entry-file index.ios.js --reset-cache --bundle-output ios/bundle/main.jsbundle --dev false --assets-dest ios/bundle/";
    }
}
