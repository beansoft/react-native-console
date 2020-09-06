package com.github.beansoft.reatnative.idea.actions;

import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.tabs.TabbedContentAction.CloseAction;
import org.jetbrains.annotations.NotNull;

/**
 * This action is used to close a content tab.
 * Created by beansoft on 2017/5/23.
 */
public class CloseTabAction extends CloseAction {
    public CloseTabAction(@NotNull Content content) {
        super(content);
    }

    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setEnabledAndVisible( myContent.isCloseable());//myManager.canCloseContents() &&
        presentation.setText(myManager.getCloseActionName());
        presentation.setIcon(PluginIcons.Cancel);
    }
}
