package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.utils.RNPathUtil;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Method;

// Show NPM project in finder/explorer
public class LocateInFinderAction extends BaseRNConsoleAction {
    public LocateInFinderAction(ReactNativeConsole terminal) {
        super(terminal, getActionName(), getActionName(), PluginIcons.Folder);
    }

    public void doAction(AnActionEvent anActionEvent) {
        String npmLocation = RNPathUtil.getRNProjectPath(getProject());

        if (npmLocation != null) {
            File file = new File(npmLocation + File.separatorChar + "package.json");
            openFile(file);
        }
    }

    // Compatible with all IDEA 2019 versions
    public static void openFile(File file) {
        try {
            Class revealFileAction = Class.forName("com.intellij.ide.actions.RevealFileAction");// IDEA 2019.3+
            Method method = revealFileAction.getMethod("openFile",  java.io.File.class);
            method.invoke(revealFileAction, file);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Class revealFileAction = Class.forName("com.intellij.ide.actions.ShowFilePathAction");// IDEA 2019.1
                Method method = revealFileAction.getMethod("openFile",  File.class);
                method.invoke(revealFileAction, file);
            } catch (Exception ex) {
                ex.printStackTrace();
                Messages.showErrorDialog("This action isn't supported on the current platform", "Cannot Open File");
//                NotificationUtils.showNotification("Open file failed:" + ex.getMessage(), NotificationType.ERROR);
            }
        }
    }



//        @Override
//        public void update(AnActionEvent e) {
//            String npmLocation = RNPathUtil.getRNProjectPath(getProject());
//            Presentation presentation = e.getPresentation();
//            presentation.setText(getActionName());
//            presentation.setEnabled(npmLocation != null);
//        }

    @NotNull
    public static String getActionName() {
        return SystemInfo.isMac ? "Reveal Project in Finder" : "Show Project in Exeplorer";
    }
}