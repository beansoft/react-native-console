package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.Utils;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.util.regex.Pattern;

public class DebugUiAction extends BaseRNConsoleAction {
    public DebugUiAction(ReactNativeConsole terminal) {
        super(terminal, "open debugger-ui", "open debugger-ui", PluginIcons.OpenChromeDebugger);
    }

    public void doAction(AnActionEvent anActionEvent) {
        String url = Messages.showInputDialog(anActionEvent.getData(PlatformDataKeys.PROJECT),
                "input url",
                "open debugger-ui",
                new ImageIcon(anActionEvent.getData(PlatformDataKeys.PROJECT) + "/resources/icons/chrome16.png"),
                "http://localhost:8081/debugger-ui",
                new InputValidator() {
                    @Override
                    public boolean checkInput(String url) {
                        Pattern pattern = Pattern
                                .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");

                        return pattern.matcher(url).matches();
                    }

                    @Override
                    public boolean canClose(String s) {
                        return true;
                    }
                });

        Utils.openUrl(url);
    }
}
