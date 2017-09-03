package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

// NPM Run start Task
public class NPMStartAction extends BaseRNConsoleNPMAction {
    public NPMStartAction(ReactNativeConsole terminal) {
        super(terminal, "React Packager", "start node server(npm run start)", PluginIcons.Execute);
    }

    protected String command() {
        return "npm run start";
    }
}
