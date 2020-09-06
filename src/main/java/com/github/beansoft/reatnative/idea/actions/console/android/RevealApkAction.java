package com.github.beansoft.reatnative.idea.actions.console.android;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoft.reatnative.idea.actions.console.LocateInFinderAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.utils.RNPathUtil;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.SystemInfo;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;

/**
 * View apk folder.
 *
 * @see com.intellij.ide.actions.ShowFilePathAction
 * @author beansoft
 * date 2020.3.1
 */
public class RevealApkAction extends BaseRNConsoleAction {
    public RevealApkAction(ReactNativeConsole terminal) {
        super(terminal, "Open APK build folder", "Open APK build output folder in Project",
            PluginIcons.RevealApk);
    }

    @Override
    public void doAction(@NotNull AnActionEvent e) {
        File file = getFile(e);
        if (file != null) {
            doOpen(file);
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(isSupported() && getFile(e) != null);
        e.getPresentation().setText(getActionName());
    }

    @Nullable
    private static File getFile(@NotNull AnActionEvent e) {
        String path = RNPathUtil.getRNProjectPath(e.getProject());
        if(path == null) {
            return null;
        }

        String androidProjectPath = RNPathUtil.getAndroidProjectPath(path);

        if (StringUtils.isNotBlank(androidProjectPath)) {
            File file = new File(androidProjectPath + "/app/build/outputs/apk".replace('/', File.separatorChar));
            if(file.exists() && file.isDirectory()) {
                return file;
            }

        }

        return null;
    }

    public static boolean isSupported() {
        return SystemInfo.isWindows || SystemInfo.isMac || SystemInfo.hasXdgOpen() ||
                Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN);
    }

    @NotNull
    public static String getActionName() {
        return SystemInfo.isMac ? "Reveal APK build output folder in Finder" : "Show APK build output folder in Exeplorer";
    }


    private static void doOpen(@NotNull File file) {
        LocateInFinderAction.openFile(file);
    }
}
