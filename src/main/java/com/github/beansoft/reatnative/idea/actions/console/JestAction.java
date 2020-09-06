package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;

public class JestAction extends BaseRNConsoleNPMAction {
    public JestAction(ReactNativeConsole terminal) {
        super(terminal, "npm test", "run Jest test", PluginIcons.Jest);
    }

    protected String command() {
        return "npm test";
    }
}
