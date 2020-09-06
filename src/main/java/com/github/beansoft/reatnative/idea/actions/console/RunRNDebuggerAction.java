package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.ui.RNConsole;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;
import com.intellij.execution.filters.BrowserHyperlinkInfo;
import com.intellij.execution.ui.ConsoleViewContentType;

import java.io.File;

/**
 * run React Native Debugger https://github.com/jhen0409/react-native-debugger
 * Contributor: https://github.com/troublediehard
 */
public class RunRNDebuggerAction extends BaseRNConsoleNPMAction {
    public RunRNDebuggerAction(ReactNativeConsole terminal) {
        super(terminal, "React Native Debugger", "run React Native Debugger https://github.com/jhen0409/react-native-debugger", PluginIcons.RNDebugger);
    }

    @Override
    public boolean beforeAction() {
        File f = new File("/Applications/React Native Debugger.app/");

        if(f.exists()) {
            return true;
        }
        else {
            RNConsole consoleView = terminal.getRNConsole(getText(), getIcon());

            if (consoleView != null) {
                consoleView.print(
                        "Can't found React Native Debugger, if you were first time running this command, make sure " +
                                "you have React Native Debugger installed on your Mac and can be located at /Applications/React Native Debugger.app/.\n" +
                                "To download and install, go to ",
                        ConsoleViewContentType.ERROR_OUTPUT);
                consoleView.printHyperlink("https://github.com/jhen0409/react-native-debugger",
                        new BrowserHyperlinkInfo("https://github.com/jhen0409/react-native-debugger"));

            }
            return false;
        }
    }

    protected String command() {
        return "open rndebugger://set-debugger-loc?host=localhost&port=8081";
    }
}
