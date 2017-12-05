package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

import java.io.File;

public class AndroidBundleAction extends BaseRNConsoleNPMAction {
    public AndroidBundleAction(ReactNativeConsole terminal) {
        super(terminal, "Android RN Bundle",
                "Create Release React Native Bundle File for Android ", PluginIcons.Deploy);
    }

    @Override
    public boolean beforeAction() {

        String npmLocation = RNPathUtil.getRNProjectPath(getProject());

        if (npmLocation == null) {
            NotificationUtils.packageJsonNotFound();
        } else {
            try {
                File f = new File(npmLocation, "android/app/src/main/assets/index.android.bundle");
                if (f.exists()) {
                    f.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    protected String command() {
        return "react-native bundle --platform android --entry-file index.android.js --reset-cache --bundle-output android/app/src/main/assets/index.android.bundle --dev false --assets-dest android/app/src/main/res/";
//            return "react-native bundle --platform android --dev false --entry-file index.android.js --bundle-output ./bundle-android/index.android.bundle --assets-dest ./bundle-android";
    }
}
