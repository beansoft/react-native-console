package com.github.beansoftapp.reatnative.idea.actions.console.java;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleActionGroup;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.ui.RNConsole;
import com.github.beansoftapp.reatnative.idea.utils.OSUtils;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Show all gradle tasks, and let the user choose one item to run.
 * TODO list gradle tasks. Using /gradlew tasks --all ?
 * @date 2018-12-127
 */
public class RunGradleTaskAction extends BaseRNConsoleActionGroup {
    public RunGradleTaskAction(ReactNativeConsole terminal) {
        super(terminal, "Run gradle tasks", "Run gradle tasks", PluginIcons.Gradle);
        setPopup(true);
    }

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent e) {
        if (e == null) return EMPTY_ARRAY;
        final Project project = e.getProject();
        if (project == null) return EMPTY_ARRAY;
        String[] devices = {"build", "integTest", "test", "bootJar", "checkstyleMain", "spotbugsMain"};
        if (devices == null || devices.length == 0) return EMPTY_ARRAY;
        final AnAction[] actions = new AnAction[devices.length];

        String inputDir = project.getBasePath();

        for (int i = 0; i < devices.length; i++) {
            String scriptName = devices[i];
            actions[i] = new BaseRNConsoleAction(terminal, "gradle " + scriptName, "gradle '" + scriptName + "'",
                PluginIcons.Gradle) {
                @Override
                public void doAction(AnActionEvent anActionEvent) {
                    RNConsole consoleView = terminal.getRNConsole(getText(), getIcon());
                    consoleView.executeShell(
                        command(), inputDir);
                }

                protected String command() {
                    if (OSUtils.isWindows()) {// https://github.com/beansoftapp/react-native-console/issues/8
                        return "gradlew.bat " + scriptName;
                    }
                    return "." + File.separator + "gradlew " + scriptName;
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
