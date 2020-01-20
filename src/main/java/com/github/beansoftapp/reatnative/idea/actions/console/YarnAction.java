package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

public class YarnAction extends BaseRNConsoleNPMAction {
    public YarnAction(ReactNativeConsole terminal) {
        super(terminal, "yarn", "yarn", PluginIcons.Yarn);
    }

    protected String command() {
        return "yarn";
    }
}
