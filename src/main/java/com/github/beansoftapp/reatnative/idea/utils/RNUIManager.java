package com.github.beansoftapp.reatnative.idea.utils;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

/**
 * UI manager service
 */
public class RNUIManager {

    public static RNUIManager getInstance(Project project) {
        return ServiceManager.getService(project, RNUIManager.class);
    }

//    private ConsoleView mFreeConsole;
//
//    public ConsoleView getConsoleView(Project project) {
//        if (mFreeConsole == null) {
//            mFreeConsole = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
//        }
//        return mFreeConsole;
//    }
}
