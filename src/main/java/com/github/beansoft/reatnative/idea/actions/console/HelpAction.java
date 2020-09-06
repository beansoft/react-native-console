package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.utils.Utils;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;
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
