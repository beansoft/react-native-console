package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleRunAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.CustomShortcutSet;

import javax.swing.*;
import java.awt.event.InputEvent;

import static java.awt.event.KeyEvent.VK_Z;

/**
 * Reloading JavaScript on Android device, tested on most android devices.
 * @version 2019-8-21 supports latest RN
 * @since 1.0.6
 * @author beansoft
 */
public class AndroidRefreshAction extends BaseRNConsoleRunAction// implements ShortcutProvider
{
    public static final CustomShortcutSet Android_REFRESH_SHORTCUT = new CustomShortcutSet(KeyStroke.getKeyStroke(
            VK_Z, InputEvent.CTRL_MASK|InputEvent.SHIFT_MASK ));

    public AndroidRefreshAction(ReactNativeConsole terminal) {
        super(terminal, "Android Reload JS(Double Tap R key)", "Android Reloading JavaScript", PluginIcons.Refresh);
//        registerCustomShortcutSet(CustomShortcutSet.fromString("Ctrl Alt A"), getComponent(), parentDisposable);
        registerCustomShortcutSet(Android_REFRESH_SHORTCUT, null);
    }

    @Override
    protected String command() {
        //return "adb shell input keyevent 82 20 66 66";//First toggle menu, then press down key to select first menu item
        // - Reload, final press enter will execute the action

        // For latest RN 0.59+, using this Double tap R on your keyboard to reload your app's code.
        return "adb shell input keyevent 46 46";
    }

//    @Override
//    public ShortcutSet getShortcut() {
//        return Android_REFRESH_SHORTCUT;
//    }
}
