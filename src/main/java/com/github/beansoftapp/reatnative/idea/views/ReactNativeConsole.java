package com.github.beansoftapp.reatnative.idea.views;

import com.intellij.openapi.components.ProjectComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * A React Native Console with console view as process runner, no more depends on terminal widget,
 * thus tabs could be reused.
 * Created by beansoft@126.com on 17/4/27.
 */
public class ReactNativeConsole implements FocusListener, ProjectComponent {
    @Override
    public void focusGained(FocusEvent e) {
//        JComponent component = myTerminalWidget != null ? myTerminalWidget.getComponent() : null;
//        if (component != null) {
//            component.requestFocusInWindow();
//        }
    }

    @Override
    public void focusLost(FocusEvent e) {
    }

    @Override
    public void projectOpened() {
    }

    @Override
    public void projectClosed() {
    }

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "ReactNativeTerminal";
    }
}
