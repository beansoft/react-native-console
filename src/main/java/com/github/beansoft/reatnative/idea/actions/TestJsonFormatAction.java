package com.github.beansoft.reatnative.idea.actions;

import com.github.beansoft.reatnative.idea.utils.RNPathUtil;
import com.intellij.execution.configurations.PtyCommandLine;
import com.intellij.execution.util.ExecUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.openapi.vfs.newvfs.ArchiveFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**

 */
public class TestJsonFormatAction extends DumbAwareAction  {
    private static final Logger LOG = Logger.getInstance(TestJsonFormatAction.class);

    public TestJsonFormatAction() {
        getTemplatePresentation().setText(getActionName());
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible( getFile(e) != null && getFile(e).getExtension().equals("json"));
        e.getPresentation().setText(getActionName());
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile file = getFile(e);
        if (file != null) {
            String npxFull = RNPathUtil.getExecuteFileFullPath("npx");
            String cmd = npxFull + " --yes json-format-cli " + file.getPresentableUrl();
//            try {
//                Process process = Runtime.getRuntime().exec(cmd);
////                process.waitFor();
//                System.out.println(process.exitValue());
//            } catch (Exception ioException) {
//                ioException.printStackTrace();
//            }
            PtyCommandLine commandLine = new PtyCommandLine(Arrays.asList(cmd.split(" ")));
            commandLine.setWorkDirectory(e.getProject().getBasePath());
//            GeneralCommandLine commandLine =
//                    RNPathUtil.createFullPathCommandLine("npx json-format-cli " + file.getPresentableUrl(), e.getProject().getBasePath());
            try {
//                RunnerUtil.genInConsole(commandLine, e.getProject(), "JsonFormat");
                ExecUtil.execAndGetOutput(commandLine);
            } catch (Exception executionException) {
                executionException.printStackTrace();
            }
        }
    }

    @Nullable
    private static VirtualFile getFile(@NotNull AnActionEvent e) {
        return findLocalFile(e.getData(CommonDataKeys.VIRTUAL_FILE));
    }

    @NotNull
    public static String getActionName() {
        return "Format JSON";
    }

    @Nullable
    public static VirtualFile findLocalFile(@Nullable VirtualFile file) {
        if (file == null || file.isInLocalFileSystem()) {
            return file;
        }

        VirtualFileSystem fs = file.getFileSystem();
        if (fs instanceof ArchiveFileSystem && file.getParent() == null) {
            return ((ArchiveFileSystem)fs).getLocalByEntry(file);
        }

        return null;
    }
}
