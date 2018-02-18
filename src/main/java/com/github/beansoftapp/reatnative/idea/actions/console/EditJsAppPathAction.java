package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.utils.Utils;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;

import java.io.File;

/**
 * Setup js app working path.
 * @author beansoft
 * @date 2018-02-18
 */
public class EditJsAppPathAction extends BaseRNConsoleAction {

    public EditJsAppPathAction(ReactNativeConsole terminal) {
        super(terminal, "Edit js working directory", "edit js project working directory", PluginIcons.EditFolder);
    }

    @Override
    public void doAction(AnActionEvent anActionEvent) {
        doEditJsProjectPath(getProject());
    }

    public static void doEditJsProjectPath(Project project) {
        String path = Messages.showInputDialog(project,
                "Specify js project work directory rather than root directory, eg ./jsapp\nThe value is stored in file .idea/.rnconsole",
                "input js working directory",
                PluginIcons.EditFolder,
                RNPathUtil.getRNProjectRawRootPathFromConfig(project),
                new InputValidator() {
                    @Override
                    public boolean checkInput(String text) {
                        boolean notEmpty = Utils.notEmpty(text);
                        if(!notEmpty) return false;
                        try {
                            File f = new File(project.getBasePath(), text);
                            return (f.isDirectory() && f.exists());
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }

                    @Override
                    public boolean canClose(String s) {
                        return true;
                    }
                });

        if(Utils.notEmpty(path))
            RNPathUtil.saveRNProjectRootPathToConfig(project, path);
    }
}