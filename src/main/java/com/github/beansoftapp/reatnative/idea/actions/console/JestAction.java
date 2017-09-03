package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

public class JestAction extends BaseRNConsoleNPMAction {
    public JestAction(ReactNativeConsole terminal) {
        super(terminal, "npm test", "run Jest test", PluginIcons.Jest);
    }

    protected String command() {
        return "npm test";
    }
}
