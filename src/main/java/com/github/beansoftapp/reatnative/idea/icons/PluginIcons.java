package com.github.beansoftapp.reatnative.idea.icons;

import com.github.beansoftapp.reatnative.swing.OverlayIcon;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

//import icons.AndroidIcons;

/**
 * Icons used by plugin.
 * Created by beansoft on 17/4/1.
 */
public class PluginIcons {
    //    public static final Icon LastEditLocation = load("/icons/last_edit_pos.png");
    public static final Icon AddGreen = load("/icons/add_green.png");
    public static final Icon Add = load("/icons/add.svg");
    public static final Icon Cancel = load("/actions/cancel.svg");
    public static final Icon Refresh = load("/actions/refresh.svg");
    public static final Icon Locate = load("/icons/locate.svg");
    //    public static final Icon Plus = load("/icons/plus.png");
    public static final Icon Npm = load("/icons/npm_16.png");
    public static final Icon Folder = load("/icons/folder.png");
    public static final Icon Yarn = load("/icons/yarn.png");
    public static final Icon newWatch = load("/icons/newWatch.png");
    public static final Icon Jest = load("/icons/jest.png");
    public static final Icon FreelineIcon = load("/icons/ReactNative.png");
    public static final Icon OpenTerminal = load("/icons/OpenTerminal_13x13.svg");
    public static final Icon Execute = load("/actions/execute.svg");
    public static final Icon Install = load("/actions/install.svg");
    public static final Icon Uninstall = load("/icons/uninstall.svg");
    public static final Icon Error = load("/icons/error.svg");;
    public static final Icon Up = load("/actions/moveUp.svg");;
    public static final Icon RESET_TO_EMPTY = Install;
    public static final Icon Lightning = load("/actions/lightning.png");
    public static final Icon Suspend = load("/actions/suspend.png");
    public static final Icon StartDebugger = load("/actions/startDebugger.png");
    public static final Icon OpenChromeDebugger = load("/icons/chrome16.png");
    public static final Icon QuickfixBulb = load("/actions/quickfixBulb.png");
    public static final Icon GC = load("/actions/gc.svg");
    public static final Icon Restart = load("/actions/restart.svg");

    public static final Icon EditConfig = load("/actions/edit.png");
    public static final Icon DevMenu = load("/icons/gearPlain.png");
    public static final Icon Link = load("/icons/link.png");
    public static final Icon UnLink = load("/actions/cancel.svg");
    public static final Icon New = load("/actions/new.svg");
    public static final Icon Reset = load("/actions/rollback.svg");

    public static final Icon Android = load("/icons/android.png");
    public static final Icon LibraryModule = load("/icons/libraryModule.png");
    public static final Icon IPhoneDevice = load("/icons/iPhone.png");
    public static final Icon IPhoneDevices = load("/icons/devices.png");

    public static final Icon IPhoneSimulator = load("/icons/iphone_simulator.png");
    public static final Icon RNDebugger = load("/icons/rn_debugger.png");
    public static final Icon Apple = load("/icons/apple.png");
    public static final Icon React = load("/icons/jsx_16.png");
    public static final Icon ResourceBundle = load("/nodes/resourceBundle.png");
    public static final Icon Archive = load("/icons/archive.png");
    public static final Icon Export = load("/icons/export.svg");
    public static final Icon Import = load("/icons/import.svg");
    public static final Icon Application = load("/icons/application.svg");
    public static final Icon QuestionDialog = load("/icons/questionDialog.svg");
    public static final Icon Compile = load("/icons/compile.png");
    public static final Icon Undeploy = load("/nodes/undeploy.png"); // 16x16
    public static final Icon Help_small = load("/general/help_small.png"); // 16x16
    public static final Icon Deploy = load("/icons/deploy.svg"); // 16x16
    public static final Icon TrackTests = load("/icons/trackTests.png"); // 16x16
    public static final Icon Run_run = load("/icons/run_run.svg"); // 12x12
    public static final Icon Help = load("/actions/help.svg"); // 16x16
    public static final Icon CommandLine = load("/icons/commandLine.png"); // 16x16
    public static final Icon Console = load("/icons/console.png"); // 16x16
    public static final Icon Console_log = load("/icons/console_log.png"); // 16x16
    public static final Icon RealDevice = load("/icons/real_device.png"); // 16x16
    public static final Icon Gradle = load("/icons/gradle.svg"); // 16x16
    public static final Icon EditFolder = load("/icons/editFolder.svg");
    public static final Icon Gear = load("/icons/gearPlain.svg");
    public static final Icon GearHover = load("/icons/gearHover.png");
    public static final Icon CocoaPods = load("/icons/cocoapods-logo.svg");

    /* Run action icon */
    public static final Icon ICON_ACTION_RUN = FreelineIcon;
    /* Tool window icon */
    public static final Icon ICON_TOOL_WINDOW = OpenTerminal;

    public static Icon NPMStart = new OverlayIcon(Npm).add(Execute);

    private static Icon load(String path) {
        try {
            return IconLoader.getIcon(path, PluginIcons.class);
        } catch (IllegalStateException e) {
            return null;
        }
    }

//    private static Icon androidLoad(String path) {
//        return load(path, AndroidIcons.class);
//    }

//    private static Icon load(String path) {
//        return load(path, AllIcons.class);
//    }
}
