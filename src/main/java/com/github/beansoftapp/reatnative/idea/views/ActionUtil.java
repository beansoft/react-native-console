package com.github.beansoftapp.reatnative.idea.views;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

public class ActionUtil {
    /**
     * Create some actions
     *
     * @param horizontal     is horizontal displayed
     * @return
     */
    public static ActionToolbar createToolbarWithActions(boolean horizontal,
                                                         AnAction... actions) {
        DefaultActionGroup group = new DefaultActionGroup();

        if (actions != null) {
            for (AnAction anAction : actions) {
                group.add(anAction);
            }
        }
        //group.addSeparator();
        return ActionManager.getInstance().createActionToolbar("unknown", group, horizontal);// horizontal
    }
}