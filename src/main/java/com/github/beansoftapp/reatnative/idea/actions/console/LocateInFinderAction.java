package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoftapp.reatnative.idea.actions.RevealFileAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;

import java.io.File;

// Show NPM project in finder/explorer
public class LocateInFinderAction extends BaseRNConsoleAction {
    public LocateInFinderAction(ReactNativeConsole terminal) {
        super(terminal, getActionName(), getActionName(), PluginIcons.Folder);
    }

    public void doAction(AnActionEvent anActionEvent) {
        String npmLocation = RNPathUtil.getRNProjectPath(getProject());

        if (npmLocation != null) {
            RevealFileAction.openFile(new File(npmLocation + File.separatorChar + "package.json"));
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
        return SystemInfo.isMac ? "Reveal Project in Finder" : "Show Project in " + com.intellij.ide.actions.RevealFileAction.getFileManagerName();
    }
}
