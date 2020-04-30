package com.github.beansoftapp.reatnative.idea.views;

import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Created by beansoft on 17/3/16.
 */
public class RNToolWindowFactory implements ToolWindowFactory, DumbAware {

    public static final String TOOL_WINDOW_ID = "RNConsole";

    public RNToolWindowFactory() {
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
//        ReactNativeTerminal.getInstance(project).init(toolWindow);
        ReactNativeConsole.getInstance(project).init(toolWindow);
    }

    public boolean isApplicable(@NotNull Project project) {
        String path = RNPathUtil.getRNProjectPath(project);
        return true;
    }
}
