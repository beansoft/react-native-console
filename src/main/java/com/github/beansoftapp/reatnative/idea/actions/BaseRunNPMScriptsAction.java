package com.github.beansoftapp.reatnative.idea.actions;

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
import java.util.ArrayList;
import java.util.List;

/**
 * 2018-01-10
 * Base action for run multiple scripts action.
 * if the command template contains an $, then a dialog brings up for you to input an argument.
 * Usage: extends, then call addActions.
 * @see #addActions(AnAction...)
 * @author beansoft
 */
public class BaseRunNPMScriptsAction extends BaseRNConsoleActionGroup {
    private List<AnAction> actionList = new ArrayList<>();

    public BaseRunNPMScriptsAction(ReactNativeConsole terminal, String text, String description, Icon icon) {
        super(terminal, text, description, icon);
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
        return actionList.toArray(new AnAction[0]);
    }

    /**
     * 子类可以向父类添加多个Action.
     * Add more children actions to this action.
     */
    public void addActions(AnAction... actions) {
        if(actions != null && actions.length > 0) {
            for(AnAction anAction : actions) {
                actionList.add(anAction);
            }
        }
    }

    protected AnAction makeAction(Icon icon, String text, String commandTemplate) {
        return makeAction(icon, text, text,
                commandTemplate);
    }

    protected AnAction makeAction(Icon icon, String text, String description, String commandTemplate) {
        if(commandTemplate.contains("$")) {
            return new BaseRNConsoleAction(terminal, text, description,
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
        } else {
            return new BaseRNConsoleAction(terminal, text, description,
                    icon) {
                @Override
                public void doAction(AnActionEvent anActionEvent) {
                        RNConsoleImpl consoleView = terminal.getRNConsole(getText(), getIcon());
                        consoleView.runNPMCI(commandTemplate);
                }
            };
        }

    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }
}