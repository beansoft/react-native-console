package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;

@Deprecated
public class ReWatchManAction extends BaseRNConsoleNPMAction {
    public ReWatchManAction(ReactNativeConsole terminal) {
        super(terminal, "re-watch project", "re-watch", PluginIcons.Jest);
    }

//        @Override
//        public void doAction(AnActionEvent anActionEvent) {
//            beforeAction();
//            terminal.runNPMCI(command(), getText(), getIcon());
//            afterAction();
//        }

    protected String command() {
        return "watchman watch-del .;watchman watch-project .";
    }
}
