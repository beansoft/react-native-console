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
 * Run on Physical iOS Device
 * @author beansoft
 */
public class RunIOSDeviceAction extends BaseRNConsoleNPMAction {
    private static final String EXEC = "react-native run-ios --device";
    private static final String IOS_DEPLOY = "ios-deploy";

    private String cmd = EXEC;

    public RunIOSDeviceAction(ReactNativeConsole terminal) {
        super(terminal, "iOS Run Device", "Run on Physical iOS Device", PluginIcons.IPhoneDevice);
    }

    @Override
    public boolean beforeAction() {
        String exePath = RNPathUtil.getExecuteFileFullPath(IOS_DEPLOY);

        if (exePath == null || IOS_DEPLOY.equals(RNPathUtil.getExecuteFileFullPath(IOS_DEPLOY))) {
//            GlobalPropertyBasedDoNotAskOption dontAsk = new GlobalPropertyBasedDoNotAskOption("react-devtools.to.show"); // TODO no use so far
            int options = Messages.showIdeaMessageDialog(getProject(),
                    "Would you like to install ios-deploy globally now?\n" +
                            "This might take one or two minutes without any console update, please wait for the final result.\n" +
                            "After that, you'll need to click this button again.",
                    "Can Not Found Ios-Deploy", new String[]{"Yes", "No"}, 0,
                    AllIcons.General.QuestionDialog, new DialogWrapper.DoNotAskOption.Adapter() {
                        @Override
                        public void rememberChoice(boolean b, int i) {

                        }
                    });
            if (options == 0) {
                cmd = "npm install -g ios-deploy";
                return true;
            } else {
                RNConsoleImpl consoleView = terminal.getRNConsole(getText(), getIcon());

                if (consoleView != null) {
                    consoleView.clear();
                    consoleView.print(
                            "Can't found " + IOS_DEPLOY + ", if you were first running this command, make sure you have ios-deploy installed globally.\n" +
                                    "To install, please run in terminal with command: \n" +
                                    "npm install -g ios-deploy\n" +
                                    "And now please connect your iPhone to USB and enable developer mode.\n\n",
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
