package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.Utils;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Open help message.
 */
public class HelpAction extends BaseRNConsoleAction {
    public HelpAction(ReactNativeConsole terminal) {
        super(terminal, "Help", "Show RN Console docs online", PluginIcons.Help);
    }

    @Override
    public void doAction(AnActionEvent anActionEvent) {
        Utils.openUrl("https://github.com/beansoftapp/react-native-console");
    }
}
