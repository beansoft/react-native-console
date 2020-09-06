package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleAndroidAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.utils.OSUtils;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;

import java.io.File;

public class AndroidBundleAction extends BaseRNConsoleAndroidAction {
    public AndroidBundleAction(ReactNativeConsole terminal) {
        super(terminal, "Android bundleRelease",
                "builds the Release with JS bundle for Android ", PluginIcons.Deploy);
    }

//    @Override
//    public boolean beforeAction() {
//
//        String npmLocation = RNPathUtil.getRNProjectPath(getProject());
//
//        if (npmLocation == null) {
//            NotificationUtils.packageJsonNotFound();
//        } else {
//            try {
//                File f = new File(npmLocation, "android/app/src/main/assets/index.android.bundle");
//                if (f.exists()) {
//                    f.delete();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return true;
//    }

//    @Override
//    protected String command() {
//        return "react-native bundle --platform android --entry-file "
//                + RNPathUtil.getIndexJSFilePath(getProject(), "index.android.js") +
//                " --reset-cache --bundle-output android/app/src/main/assets/index.android.bundle --dev false --assets-dest android/app/src/main/res/";
////            return "react-native bundle --platform android --dev false --entry-file index.android.js --bundle-output ./bundle-android/index.android.bundle --assets-dest ./bundle-android";
//    }


    protected String command() {
        if (OSUtils.isWindows()) {// https://github.com/beansoftapp/react-native-console/issues/8
            return "gradlew.bat bundleRelease";
        }
        return "." + File.separator + "gradlew bundleRelease";//bundleReleaseJsAndAssets https://github.com/beansoftapp/react-native-console/issues/38
    }
}
