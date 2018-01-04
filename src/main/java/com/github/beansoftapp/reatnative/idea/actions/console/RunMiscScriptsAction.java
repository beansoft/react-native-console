package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleActionGroup;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.RNConsoleImpl;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 2018-01-04
 * Dependencies management for yarn and npm
 * TODO dev depencies and global deps
 * @author beansoft
 * run misc add commands */
public class RunMiscScriptsAction extends BaseRNConsoleActionGroup {
    public RunMiscScriptsAction(ReactNativeConsole terminal) {
        super(terminal, "Add dependencies", "Run scripts to add dependencies", PluginIcons.Add);
        setPopup(true);
    }

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent e) {
        if (e == null) {
            return EMPTY_ARRAY;
        }
        final Project project = e.getProject();
        if (project == null) {
            return EMPTY_ARRAY;
        }

        final AnAction[] actions = new AnAction[2];

        actions[0] = makeAction(PluginIcons.yarn, "yarn add dependencies", "yarn add $");
        actions[1] = makeAction(PluginIcons.Npm, "npm add dependencies", "npm i $ --save");


        return actions;
    }

    protected AnAction makeAction(Icon icon, String text, String commandTemplate) {
        return new BaseRNConsoleAction(terminal, text, text,
                icon) {
            @Override
            public void doAction(AnActionEvent anActionEvent) {
                String packageName = Messages.showInputDialog(anActionEvent.getData(PlatformDataKeys.PROJECT),
                        "Input package name",
                        "Package",
                        icon,
                        "react-native-http",
                        new InputValidator() {
                            @Override
                            public boolean checkInput(String input) {
                                return input.trim().length() > 0;
                            }

                            @Override
                            public boolean canClose(String s) {
                                return true;
                            }
                        });
                if(packageName != null && packageName.trim().length() > 0) {
                    RNConsoleImpl consoleView = terminal.getRNConsole(getText(), getIcon());
                    consoleView.runNPMCI(
                            commandTemplate.replace("$", packageName.trim()));
                }
            }
        };
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }
}