package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRunNPMScriptsAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

/**
 * @date 2018-01-10
 * Dependencies management for react native
 * @author beansoft
 * run misc add commands */
public class RunRNScriptsAction extends BaseRunNPMScriptsAction {

    public RunRNScriptsAction(ReactNativeConsole terminal) {
        super(terminal, "RN dependencies", "Manage React Native dependencies", PluginIcons.React);
//        addActions(makeAction(PluginIcons.Execute, "react-native start", "starts the webserver", "react-native start"));
        addActions(makeAction(PluginIcons.Install, "react-native install", "react-native install and link native dependencies", "react-native install $"));
        addActions(makeAction(PluginIcons.Uninstall, "react-native uninstall", "react-native uninstall and unlink native dependencies", "react-native uninstall $"));
        addActions(new RNLinkAction(terminal));
        addActions(makeAction(PluginIcons.UnLink, "react-native unlink", "react-native unlink native dependency", "react-native unlink $"));
        addActions(new RNUpgradeAction(terminal));
//        addActions(makeAction(PluginIcons.New, "react-native new-library", "generates a native library bridge", "react-native new-library --name $"));
    }
}