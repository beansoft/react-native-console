package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.utils.IdeaMessages;
import com.github.beansoft.reatnative.idea.utils.RNPathUtil;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

/**
 * Edit metro port.
 * @author beansoft
 * @date 2018-09-04
 */
public class EditMetroPortAction extends BaseRNConsoleAction {

    public EditMetroPortAction(ReactNativeConsole terminal) {
        super(terminal, "Edit metro port", "edit metro port", PluginIcons.EditFolder);
    }

    @Override
    public void doAction(AnActionEvent anActionEvent) {
        doEditPort(getProject());
    }

    /**
     * Edit given project's metro port.
     * @param project
     */
    public static void doEditPort(Project project) {
        String port = IdeaMessages.showInputDialog(project,
                "Current port is " + RNPathUtil.getRNMetroPortFromConfig(project) +
                        "\nEdit metro port, input empty value to disable it.\nThe value is stored in file .idea/.rnconsole",
                "Edit Metro Port",
                PluginIcons.EditFolder,
                RNPathUtil.getRNMetroPortFromConfig(project),
                null);

        RNPathUtil.saveRNMetroPortToConfig(project, port);
    }
}