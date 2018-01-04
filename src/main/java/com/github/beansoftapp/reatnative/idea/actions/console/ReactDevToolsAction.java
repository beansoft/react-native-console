package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleRunAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.views.RNConsoleImpl;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;

/**
 * Open react-devtools
 *
 * @version 1.1.1 2017-12-05
 */
public class ReactDevToolsAction extends BaseRNConsoleRunAction {
    private static final String EXEC = "react-devtools";
    private String cmd = EXEC;

    public ReactDevToolsAction(ReactNativeConsole terminal) {
        super(terminal, "react-devtools",
                "run globally installed react-devtools", PluginIcons.React);
    }

    @Override
    public boolean beforeAction() {
        String exePath = RNPathUtil.getExecuteFileFullPath(EXEC);
        if (exePath == null || EXEC.equals(RNPathUtil.getExecuteFileFullPath(EXEC))) {
//            GlobalPropertyBasedDoNotAskOption dontAsk = new GlobalPropertyBasedDoNotAskOption("react-devtools.to.show"); // TODO no use so far
            int options = Messages.showIdeaMessageDialog(getProject(),
                    "Would you like to install react-devtools globally now?\n" +
                            "This might take one or two minutes without any console update, please wait for the final result.\n" +
                            "After that, you'll need to click this button again.",
                    "Can Not Found React-Devtools", new String[]{"Yes", "No"}, 0,
                    AllIcons.General.QuestionDialog, new DialogWrapper.DoNotAskOption.Adapter() {
                        @Override
                        public void rememberChoice(boolean b, int i) {

                        }
                    });
            if (options == 0) {
                cmd = "npm install -g react-devtools";
                return true;
            } else {
                RNConsoleImpl consoleView = terminal.getRNConsole(getText(), getIcon());

                if (consoleView != null) {
                    consoleView.clear();
                    consoleView.print(
                            "Can't found " + EXEC + ", if you were first running this command, make sure you have react-devtools installed globally.\n" +
                                    "To install by yourself, please run in terminal with command: \n" +
                                    "npm install -g react-devtools\n\n",
                            ConsoleViewContentType.ERROR_OUTPUT);

                }
                return false;
            }

        }

        cmd = EXEC;
        return true;
    }

    @Override
    protected String command() {
        return cmd;
    }
}