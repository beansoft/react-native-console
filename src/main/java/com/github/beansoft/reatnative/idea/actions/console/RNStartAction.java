package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;

// React Native start React Packager Task
public class RNStartAction extends BaseRNConsoleNPMAction {
    public RNStartAction(ReactNativeConsole terminal) {
        super(terminal, "React Packager", "starts the React packager webserver(react-native start)", PluginIcons.Execute);
    }

    protected String command() {
        return "react-native start" + getMetroPortParams();
    }
}
