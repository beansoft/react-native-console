package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

// React Native start React Packager Task
public class RNStartAction extends BaseRNConsoleNPMAction {
    public RNStartAction(ReactNativeConsole terminal) {
        super(terminal, "React Packager", "starts the React packager webserver(react-native start)", PluginIcons.Execute);
    }

    protected String command() {
        return "react-native start" + getMetroPortParams();
    }
}
