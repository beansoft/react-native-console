package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;

public class YarnAction extends BaseRNConsoleNPMAction {
    public YarnAction(ReactNativeConsole terminal) {
        super(terminal, "yarn", "yarn", PluginIcons.Yarn);
    }

    protected String command() {
        return "yarn";
    }
}
