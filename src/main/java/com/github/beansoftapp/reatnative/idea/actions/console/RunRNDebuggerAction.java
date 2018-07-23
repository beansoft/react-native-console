package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

public class RunRNDebuggerAction extends BaseRNConsoleNPMAction {
    public RunRNDebuggerAction(ReactNativeConsole terminal) {
        super(terminal, "React Native Debugger", "run React Native Debugger https://github.com/jhen0409/react-native-debugger", PluginIcons.RNDebugger);
    }

    protected String command() {
        return "open rndebugger://set-debugger-loc?host=localhost&port=8081";
    }
}
