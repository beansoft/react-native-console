package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleRunAction;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.icons.AllIcons;

/**
 * Reloading JavaScript on Android device, tested on Samsung and MOTO XStyle only.
 *
 * @since 1.0.6
 */
public class AndroidRefreshAction extends BaseRNConsoleRunAction {
    public AndroidRefreshAction(ReactNativeConsole terminal) {
        super(terminal, "Android Reload JS", "Android Reloading JavaScript(beta)", AllIcons.Actions.Refresh);
    }

    @Override
    protected String command() {
        return "adb shell input keyevent 82 20 66 66";//First toggle menu, then press down key to select first menu item
        // - Relead, final press enter will execute the action
    }
}
