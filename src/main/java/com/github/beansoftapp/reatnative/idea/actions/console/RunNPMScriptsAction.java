package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.utils.npm.NPMParser;
import com.github.beansoftapp.reatnative.idea.views.RNConsoleImpl;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.impl.ActionManagerImpl;
import com.intellij.openapi.actionSystem.impl.MenuItemPresentationFactory;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.impl.content.ToolWindowContentUi;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.File;

/** Show all package.json scripts, and let the user choose one item to run */
public class RunNPMScriptsAction extends BaseRNConsoleNPMAction {
    public RunNPMScriptsAction(ReactNativeConsole terminal) {
        super(terminal, "Run npm scripts", "Run npm scripts", PluginIcons.Npm);
    }

    @Override
    public void doAction(AnActionEvent e) {
        doRun(e);

//            ApplicationManager.getApplication().executeOnPooledThread(() -> {
//
//            });
    }

    void doRun(AnActionEvent e) {
        // TODO
        java.util.List<String> devices = NPMParser.parseScripts(new File(RNPathUtil.getRNProjectPath(getProject())));
        ApplicationManager.getApplication().invokeLater(() -> {
            if (devices == null) {
                NotificationUtils.errorMsgDialog("Sorry, no scripts found in package.json!");
                return;
            }

            int x = 0;
            int y = 0;
            InputEvent inputEvent = e.getInputEvent();
            if (inputEvent instanceof MouseEvent) {
                x = ((MouseEvent) inputEvent).getX();
                y = ((MouseEvent) inputEvent).getY();
            }
            showDevicesPopup(inputEvent.getComponent(), x, y, createDevicesPopupGroup(devices));
        });

    }

    // Show a ios device list popup menu
    private void showDevicesPopup(Component component, int x, int y, DefaultActionGroup defaultActionGroup) {
        ActionPopupMenu popupMenu =
                ((ActionManagerImpl) ActionManager.getInstance())
                        .createActionPopupMenu(ToolWindowContentUi.POPUP_PLACE, defaultActionGroup,
                                new MenuItemPresentationFactory(false));// don't set forceHide to true, otherwise icons will be hidden in menu item
        popupMenu.getComponent().show(component, x, y);
    }

    // Generate a ios device list
    private DefaultActionGroup createDevicesPopupGroup(java.util.List<String> devices) {
        DefaultActionGroup group = new DefaultActionGroup();
        devices.forEach(scriptName -> {
            if (scriptName != null) {
                group.add(new BaseRNConsoleAction(super.terminal, "npm run " + scriptName, "Run npm script: '" + scriptName + "'",
                        PluginIcons.Npm) {
                    @Override
                    public void doAction(AnActionEvent anActionEvent) {
                        RNConsoleImpl consoleView = terminal.getRNConsole(getText(), getIcon());
                        consoleView.runNPMCI(
                                "npm run " + scriptName);
                    }
                });
            }
        });


        return group;
    }

    @Override
    protected String command() {
        return null;
    }
}