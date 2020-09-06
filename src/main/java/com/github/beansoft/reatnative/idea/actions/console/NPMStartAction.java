package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;

// NPM Run start Task
public class NPMStartAction extends BaseRNConsoleNPMAction {
    public NPMStartAction(ReactNativeConsole terminal) {
        super(terminal, "NPM Start", "npm run start", PluginIcons.NPMStart);
    }

    protected String command() {
        return "npm run start";
    }
}
