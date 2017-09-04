package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleRunAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

public class AndroidDevMenuAction extends BaseRNConsoleRunAction {
    public AndroidDevMenuAction(ReactNativeConsole terminal) {
        super(terminal, "Android Dev Menu", "Open Android Dev Menu", PluginIcons.DevMenu);
    }

    @Override
    protected String command() {
        return "adb shell input keyevent 82";
    }
}
