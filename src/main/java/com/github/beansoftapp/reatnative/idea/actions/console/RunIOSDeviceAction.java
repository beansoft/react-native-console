package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.RNConsoleImpl;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.execution.ui.ConsoleViewContentType;

public class RunIOSDeviceAction extends BaseRNConsoleNPMAction {
    public RunIOSDeviceAction(ReactNativeConsole terminal) {
        super(terminal, "iOS Run Device", "Run on Physical iOS Device", PluginIcons.IPhoneDevice);
    }

    public boolean beforeAction() {
        RNConsoleImpl consoleView = terminal.getRNConsole(getText(), getIcon());
        if (consoleView != null) {
            consoleView.print(
                    "If you were first running this command, make sure you have ios-deploy installed globally.\n" +
                            "To install, please run in terminal with command: \n" +
                            "npm install -g ios-deploy\n" +
                            "And now please connect your iPhone to USB and enable developer mode.\n\n",
                    ConsoleViewContentType.SYSTEM_OUTPUT);
        }
        return true;
    }

    protected String command() {
        return "react-native run-ios --device";
    }
}
