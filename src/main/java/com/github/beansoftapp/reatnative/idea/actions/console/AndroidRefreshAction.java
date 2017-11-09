package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleRunAction;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.CustomShortcutSet;

import javax.swing.*;
import java.awt.event.InputEvent;

import static java.awt.event.KeyEvent.VK_Z;

/**
 * Reloading JavaScript on Android device, tested on Samsung and MOTO XStyle only.
 *
 * @since 1.0.6
 */
public class AndroidRefreshAction extends BaseRNConsoleRunAction// implements ShortcutProvider
{
    public static final CustomShortcutSet Android_REFRESH_SHORTCUT = new CustomShortcutSet(KeyStroke.getKeyStroke(
            VK_Z, InputEvent.CTRL_MASK|InputEvent.SHIFT_MASK ));

    public AndroidRefreshAction(ReactNativeConsole terminal) {
        super(terminal, "Android Reload JS", "Android Reloading JavaScript(beta)", AllIcons.Actions.Refresh);
//        registerCustomShortcutSet(CustomShortcutSet.fromString("Ctrl Alt A"), getComponent(), parentDisposable);
        registerCustomShortcutSet(Android_REFRESH_SHORTCUT, null);
    }

    @Override
    protected String command() {
        return "adb shell input keyevent 82 20 66 66";//First toggle menu, then press down key to select first menu item
        // - Relead, final press enter will execute the action
    }

//    @Override
//    public ShortcutSet getShortcut() {
//        return Android_REFRESH_SHORTCUT;
//    }
}