package com.github.beansoftapp.reatnative.idea.icons;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
//import icons.AndroidIcons;

import javax.swing.*;

/**
 * Created by pengwei on 16/9/15.
 */
public class PluginIcons {

    public static final Icon FreelineIcon = load("/icons/react.png");
    public static final Icon OpenTerminal = load("/icons/OpenTerminal.png");
    public static final Icon Execute = intellijLoad("/actions/execute.png");
    public static final Icon Install = intellijLoad("/actions/install.png");
    public static final Icon Lightning = intellijLoad("/actions/lightning.png");
    public static final Icon Suspend = intellijLoad("/actions/suspend.png");
    public static final Icon StartDebugger = intellijLoad("/actions/startDebugger.png");
    public static final Icon OpenChromeDebugger = load("/icons/chrome.png");
    public static final Icon QuickfixBulb = intellijLoad("/actions/quickfixBulb.png");
    public static final Icon GC = intellijLoad("/actions/gc.png");
    public static final Icon GradleSync = load("/icons/gradlesync.png");
    public static final Icon EditConfig = intellijLoad("/actions/edit.png");
    public static final Icon DevMenu = load("/icons/dev.png");
    public static final Icon Link = load("/icons/link.png");
    public static final Icon Android = load("/icons/Android.png");
    public static final Icon IPhone = load("/icons/Apple.png");
    public static final Icon React = load("/icons/react.png");

    /* Run action icon */
    public static final Icon ICON_ACTION_RUN = FreelineIcon;
    /* Tool window icon */
    public static final Icon ICON_TOOL_WINDOW = OpenTerminal;

    private static Icon load(String path) {
        try {
            return IconLoader.getIcon(path, PluginIcons.class);
        } catch (IllegalStateException e) {
            return null;
        }
    }

//    private static Icon androidLoad(String path) {
//        return IconLoader.getIcon(path, AndroidIcons.class);
//    }

    private static Icon intellijLoad(String path) {
        return IconLoader.getIcon(path, AllIcons.class);
    }
}
