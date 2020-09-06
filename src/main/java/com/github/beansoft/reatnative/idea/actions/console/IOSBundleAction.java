package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.utils.NotificationUtils;
import com.github.beansoft.reatnative.idea.utils.RNPathUtil;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;

import java.io.File;

public class IOSBundleAction extends BaseRNConsoleNPMAction {
    public IOSBundleAction(ReactNativeConsole terminal) {
        super(terminal, "iOS RN Bundle",
                "builds the Release javascript bundle for offline use of iOS", PluginIcons.Deploy);
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
        return "react-native bundle --platform ios --entry-file "
                + RNPathUtil.getIndexJSFilePath(getProject(), "index.ios.js") +
                " --reset-cache --bundle-output ios/bundle/main.jsbundle --dev false --assets-dest ios/bundle/";
    }
}
