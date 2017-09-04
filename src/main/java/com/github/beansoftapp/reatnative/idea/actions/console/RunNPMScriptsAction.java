package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleActionGroup;
import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.utils.npm.NPMParser;
import com.github.beansoftapp.reatnative.idea.views.RNConsoleImpl;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.execution.testframework.sm.runner.history.actions.ImportTestsFromHistoryAction;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionManagerImpl;
import com.intellij.openapi.actionSystem.impl.MenuItemPresentationFactory;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.impl.content.ToolWindowContentUi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.File;

/** Show all package.json scripts, and let the user choose one item to run */
public class RunNPMScriptsAction extends BaseRNConsoleActionGroup {
    public RunNPMScriptsAction(ReactNativeConsole terminal) {
        super(terminal, "Run npm scripts", "Run npm scripts", PluginIcons.Npm);
        setPopup(true);
    }

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent e) {
        if (e == null) return EMPTY_ARRAY;
        final Project project = e.getProject();
        if (project == null) return EMPTY_ARRAY;
        java.util.List<String> devices = NPMParser.parseScripts(new File(RNPathUtil.getRNProjectPath(project), "package.json" ) );
        if (devices == null || devices.size() == 0) return EMPTY_ARRAY;
        final AnAction[] actions = new AnAction[devices.size()];

        for (int i = 0; i < devices.size(); i++) {
            String scriptName = devices.get(i);
            actions[i] = new BaseRNConsoleAction(terminal, "npm " + scriptName, "npm run '" + scriptName + "'",
                    PluginIcons.Npm) {
                @Override
                public void doAction(AnActionEvent anActionEvent) {
                    RNConsoleImpl consoleView = terminal.getRNConsole(getText(), getIcon());
                    consoleView.runNPMCI(
                            "npm run " + scriptName);
                }
            };
        }

        return actions;
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }
}