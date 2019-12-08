package com.github.beansoftapp.reatnative.idea.actions;
import com.github.beansoftapp.reatnative.idea.actions.console.LocateInFinderAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.openapi.vfs.newvfs.ArchiveFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;

/**
 * Just a delegate class with icon support, adapt for all IDEA versions.
 * This helpful action opens a file or directory in a system file manager.
 *
 * @see com.intellij.ide.actions.ShowFilePathAction
 * @author beansoft
 * date 2019.12.8
 */
public class RevealFileAction extends DumbAwareAction  {
    private static final Logger LOG = Logger.getInstance(RevealFileAction.class);

    public RevealFileAction() {
        getTemplatePresentation().setText(getActionName());
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(isSupported() && getFile(e) != null);
        e.getPresentation().setText(getActionName());
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile file = getFile(e);
        if (file != null) {
            openFile(new File(file.getPresentableUrl()));
        }
    }

    @Nullable
    private static VirtualFile getFile(@NotNull AnActionEvent e) {
        return findLocalFile(e.getData(CommonDataKeys.VIRTUAL_FILE));
    }

    public static boolean isSupported() {
        return SystemInfo.isWindows || SystemInfo.isMac || SystemInfo.hasXdgOpen() ||
                Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN);
    }

    @NotNull
    public static String getActionName() {
        return SystemInfo.isMac ? "Reveal Project in Finder" : "Show Project in Exeplorer";
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

//    public static void showDialog(Project project, String message, String title, @NotNull File file, @Nullable DialogWrapper.DoNotAskOption option) {
//        String ok = getActionName();
//        String cancel = IdeBundle.message("action.close");
//        if (Messages.showOkCancelDialog(project, message, title, ok, cancel, Messages.getInformationIcon(), option) == Messages.OK) {
//            openFile(file);
//        }
//    }

    /**
     * Opens a system file manager with given file's parent directory open and the file highlighted in it
     * (note that not all platforms support highlighting).
     */
    public static void openFile(@NotNull File file) {
        doOpen(file);
    }

    /**
     * Opens a system file manager with given directory open in it.
     */
    public static void openDirectory(@NotNull File directory) {
        if (!directory.isDirectory()) {
            LOG.info("not a directory: " + directory);
            return;
        }

        doOpen(directory.getAbsoluteFile());
    }

    private static void doOpen(@NotNull File file) {
        LocateInFinderAction.openFile(file);
    }




}
