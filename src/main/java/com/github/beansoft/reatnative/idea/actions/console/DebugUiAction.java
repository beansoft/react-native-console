package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.utils.Utils;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.util.regex.Pattern;

// Open chrome debugger ui
public class DebugUiAction extends BaseRNConsoleAction {
    private static final Pattern pattern = Pattern
            .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");

    public DebugUiAction(ReactNativeConsole terminal) {
        super(terminal, "open debugger-ui", "open debugger-ui", PluginIcons.OpenChromeDebugger);
    }

    @Override
    public void doAction(AnActionEvent anActionEvent) {
        String url = Messages.showInputDialog(anActionEvent.getData(PlatformDataKeys.PROJECT),
                "input url",
                "open debugger-ui",
                new ImageIcon(anActionEvent.getData(PlatformDataKeys.PROJECT) + "/resources/icons/chrome16.png"),
                "http://localhost:8081/debugger-ui",
                new InputValidator() {
                    @Override
                    public boolean checkInput(String url) {
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
