package com.github.beansoftapp.reatnative.idea.icons;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
//import icons.AndroidIcons;

import javax.swing.*;

/**
 * Icons used by plugin.
 * Created by beansoft on 17/4/1.
 */
public class PluginIcons {
//    public static final Icon LastEditLocation = load("/icons/last_edit_pos.png");
    public static final Icon Folder = load("/icons/folder.png");
    public static final Icon yarn = load("/icons/yarn.png");
    public static final Icon newWatch = load("/icons/newWatch.png");
    public static final Icon Jest = load("/icons/jest.png");
    public static final Icon FreelineIcon = load("/icons/ReactNative.png");
    public static final Icon OpenTerminal = load("/icons/OpenTerminal.png");
    public static final Icon Execute = intellijLoad("/actions/execute.png");
    public static final Icon Install = intellijLoad("/actions/install.png");
    public static final Icon Lightning = intellijLoad("/actions/lightning.png");
    public static final Icon Suspend = intellijLoad("/actions/suspend.png");
    public static final Icon StartDebugger = intellijLoad("/actions/startDebugger.png");
    public static final Icon OpenChromeDebugger = load("/icons/chrome16.png");
    public static final Icon QuickfixBulb = intellijLoad("/actions/quickfixBulb.png");
    public static final Icon GC = intellijLoad("/actions/gc.png");

    public static final Icon EditConfig = intellijLoad("/actions/edit.png");
    public static final Icon DevMenu = load("/general/gearPlain.png");
    public static final Icon Link = load("/icons/link.png");
    public static final Icon Android = load("/icons/android.png");
    public static final Icon LibraryModule = load("/icons/libraryModule.png");
    public static final Icon IPhoneDevice = load("/icons/iPhone.png");
    public static final Icon IPhoneDevices = load("/icons/devices.png");

    public static final Icon IPhoneSimulator = load("/icons/iphone_simulator.png");
    public static final Icon Apple = load("/icons/apple.png");
    public static final Icon React = load("/icons/jsx_16.png");
    public static final Icon ResourceBundle = intellijLoad("/nodes/resourceBundle.png");
    public static final Icon Archive = intellijLoad("/fileTypes/archive.png");
    public static final Icon Export = intellijLoad("/actions/export.png");
    public static final Icon Compile = load("/icons/compile.png");
    public static final Icon Undeploy = IconLoader.getIcon("/nodes/undeploy.png"); // 16x16
    public static final Icon Help_small = IconLoader.getIcon("/general/help_small.png"); // 16x16
    public static final Icon Deploy = IconLoader.getIcon("/nodes/deploy.png"); // 16x16
    public static final Icon TrackTests = IconLoader.getIcon("/runConfigurations/trackTests.png"); // 16x16
    public static final Icon Run_run = IconLoader.getIcon("/runConfigurations/testState/run_run.png"); // 12x12
    public static final Icon Help = IconLoader.getIcon("/actions/help.png"); // 16x16
    public static final Icon CommandLine = IconLoader.getIcon("/debugger/commandLine.png"); // 16x16
    public static final Icon Console = IconLoader.getIcon("/debugger/console.png"); // 16x16
    public static final Icon Console_log = IconLoader.getIcon("/debugger/console_log.png"); // 16x16
    public static final Icon RealDevice = IconLoader.getIcon("/icons/real_device.png"); // 16x16

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
