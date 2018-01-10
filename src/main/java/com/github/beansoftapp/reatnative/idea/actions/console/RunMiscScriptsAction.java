package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRunNPMScriptsAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

/**
 * 2018-01-04
 * Dependencies management for yarn and npm
 * TODO dev depencies and global deps
 * @author beansoft
 * run misc add commands */
public class RunMiscScriptsAction extends BaseRunNPMScriptsAction {
    public RunMiscScriptsAction(ReactNativeConsole terminal) {
        super(terminal, "Add dependencies", "Run scripts to add dependencies", PluginIcons.AddGreen);
        addActions(makeAction(PluginIcons.yarn, "yarn add dependencies", "yarn add $"));
        addActions(makeAction(PluginIcons.yarn, "yarn add dev dependencies", "yarn add --dev $"));
        addActions(makeAction(PluginIcons.yarn, "yarn add global dependencies", "yarn global add $"));
        addActions(makeAction(PluginIcons.Npm, "npm add dependencies", "npm i $ --save"));
        addActions(makeAction(PluginIcons.Npm, "npm add dev dependencies", "npm i $ --save-dev"));
        addActions(makeAction(PluginIcons.Npm, "npm add global dependencies", "npm i $ -g"));
    }

}