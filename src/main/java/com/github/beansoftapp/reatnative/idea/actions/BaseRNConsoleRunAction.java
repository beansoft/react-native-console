package com.github.beansoftapp.reatnative.idea.actions;

import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.utils.npm.NPMParser;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;
import java.io.File;

/**
 * An action to run general commands, doesn't care about directory.
 * Created by beansoft on 2017/5/25.
 */
public abstract class BaseRNConsoleRunAction extends BaseRNConsoleAction {

    public BaseRNConsoleRunAction(ReactNativeConsole terminal, String text) {
        super(terminal, text);
    }

    public BaseRNConsoleRunAction(ReactNativeConsole terminal, String text, String description, Icon icon) {
        super(terminal, text, description, icon);
    }

    @Override
    public void doAction(AnActionEvent anActionEvent) {
        if(beforeAction()) {
            terminal.executeShell(rnVersionCommand(), null, getText(), getIcon());
            afterAction();
        }
    }

    protected String rnVersionCommand() {
        String command = command();
        if(command != null && command.startsWith("react-native")) {
            String version = NPMParser.parseRNVersion(new File(RNPathUtil.getRNProjectPath(project), "package.json" ) );
            if(version != null && version.compareTo("0.60") >= 0) {
                command = command.replaceFirst("react-native", "yarn react-native");
            }
        }


        return command;
    }

    // Some action before execute commands, eg mkdir through API or shell
    public boolean beforeAction() {
        return true;
    }

    // Some action after execute commands, eg clean dir through API or shell
    public void afterAction() {
    }

    // single line command to run
    protected abstract String command();

}
