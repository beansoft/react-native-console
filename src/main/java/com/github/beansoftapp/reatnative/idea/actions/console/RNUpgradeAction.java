package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.views.RNConsoleImpl;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;

/**
 * Add react-native git upgrade
 * @date 2018-01-10
 */
public class RNUpgradeAction extends BaseRNConsoleNPMAction {
    private static final String EXEC = "react-native-git-upgrade";
    private static final String INSTALL_CMD = "npm install -g react-native-git-upgrade";
    private String cmd = EXEC;

    public RNUpgradeAction(ReactNativeConsole terminal) {
        super(terminal, "react-native git upgrade",
                "upgrade your app's template files to the latest version; " +
                        "run this after updating the react-native version in your package.json and running npm install",
                PluginIcons.Up);
    }
    @Override
    public boolean beforeAction() {
        String exePath = RNPathUtil.getExecuteFileFullPath(EXEC);

        if (exePath == null || EXEC.equals(RNPathUtil.getExecuteFileFullPath(EXEC))) {
            int options = Messages.showIdeaMessageDialog(getProject(),
                    "Would you like to install " + EXEC + " globally now?\n" +
                            "This might take one or two minutes without any console update, please wait for the final result.\n" +
                            "After that, you'll need to click this button again.",
                    "Can Not Found " + EXEC, new String[]{"Yes", "No"}, 0,
                    AllIcons.General.QuestionDialog, new DialogWrapper.DoNotAskOption.Adapter() {
                        @Override
                        public void rememberChoice(boolean b, int i) {

                        }
                    });
            if (options == 0) {
                cmd = INSTALL_CMD;
                return true;
            } else {
                RNConsoleImpl consoleView = terminal.getRNConsole(getText(), getIcon());

                if (consoleView != null) {
                    consoleView.clear();
                    consoleView.print(
                            "Can't found " + EXEC + ", if you were first running this command, make sure you have " + EXEC + " installed globally.\n" +
                                    "To install, please run in terminal with command: \n" +
                                    INSTALL_CMD +
                                    "\n\n",
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
