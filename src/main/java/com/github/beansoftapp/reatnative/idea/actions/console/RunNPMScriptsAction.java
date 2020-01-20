package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleActionGroup;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.ui.RNConsole;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.utils.npm.NPMParser;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        java.util.List<String> scripts = NPMParser.parseScripts(new File(RNPathUtil.getRNProjectPath(project), "package.json" ) );
        if (scripts == null || scripts.size() == 0) return EMPTY_ARRAY;
        final AnAction[] actions = new AnAction[scripts.size()];

        for (int i = 0; i < scripts.size(); i++) {
            String scriptName = scripts.get(i);
            actions[i] = new BaseRNConsoleAction(terminal, "npm " + scriptName, "npm run '" + scriptName + "'",
                    PluginIcons.Npm) {
                @Override
                public void doAction(AnActionEvent anActionEvent) {
//                    try {
//                        ShRunner shRunner  = ServiceManager.getService(project, ShRunner.class);
//                        if (shRunner == null || !shRunner.isAvailable(project) ) {
//                            RNConsole consoleView = terminal.getRNConsole(getText(), getIcon());
//                            consoleView.runNPMCI(
//                                    "npm run \"" + scriptName + "\"");
//                        }
//                        shRunner.run("npm run \"" + scriptName + "\"\n", RNPathUtil.getRNProjectPath(project));
//                        return;
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
                    RNConsole consoleView = terminal.getRNConsole(getText(), getIcon());
                    consoleView.runNPMCI(
                            "npm run \"" + scriptName + "\"");
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
