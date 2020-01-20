package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRunNPMScriptsAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

/**
 * @date 2020-01-20
 * @author beansoft
 * run misc CocoPods commands */
public class RunCocoPodsAction extends BaseRunNPMScriptsAction {
    public RunCocoPodsAction(ReactNativeConsole terminal) {
        super(terminal, "cocoapods", "Manage iOS cocoapods", PluginIcons.CocoaPods);
//        addActions(makeAction(PluginIcons.New, "pod init", "pod init", "pod init"));
        addActions(makeAction(PluginIcons.Install, "pod install", "pod install", "pod install "));
        addActions(makeAction(PluginIcons.Up, "pod update", "pod update", "pod update"));
    }
}
