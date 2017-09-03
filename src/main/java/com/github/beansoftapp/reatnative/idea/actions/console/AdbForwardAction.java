package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleRunAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

/**
 * 物理设备, 转发请求.
 * If you're on a physical device connected to the same machine,
 * run 'adb reverse tcp:8081 tcp:8081' to forward requests from your device
 */
public class AdbForwardAction extends BaseRNConsoleRunAction {
    public AdbForwardAction(ReactNativeConsole terminal) {
        super(terminal, "Forward Android Request",
                "forward Android device request to this machine", PluginIcons.Link);
    }

    @Override
    protected String command() {
        return "adb reverse tcp:8081 tcp:8081";
    }
}
