package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleRunAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;

public class AndroidDevMenuAction extends BaseRNConsoleRunAction {
    public AndroidDevMenuAction(ReactNativeConsole terminal) {
        super(terminal, "Android Dev Menu", "Open Android Dev Menu", PluginIcons.DevMenu);
    }

    @Override
    protected String command() {
        return "adb shell input keyevent 82";
    }
}
