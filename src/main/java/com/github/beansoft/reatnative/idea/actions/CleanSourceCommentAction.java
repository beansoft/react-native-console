package com.github.beansoft.reatnative.idea.actions;

import com.github.beansoft.reatnative.idea.utils.PsiDebugUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.command.undo.UndoUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;

/**
 * Clean comments from source code, supports all types of files if your IDEA can parse it, such as Java, JavaScript, SQL.
 * Please use WebStorm or IDEA Ultimate to delete comments in ES files.
 * see de.plushnikov.intellij.plugin.action.delombok.AbstractDelombokAction
 * @author beansoft
 * 2020.4.21
 */
public class CleanSourceCommentAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    final Project project = event.getProject();
    if (project == null) {
      return;
    }

    PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
    if (psiDocumentManager.hasUncommitedDocuments()) {
      psiDocumentManager.commitAllDocuments();
    }

    final DataContext dataContext = event.getDataContext();
    final Editor editor = PlatformDataKeys.EDITOR.getData(dataContext);

    if (null != editor) {
      final PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
      if (null != psiFile) {
        WriteCommandAction.writeCommandAction(project, psiFile).run(() ->
                {
                  PsiDebugUtil.psiToString(psiFile, false, true);
//                StringBuilder data = new StringBuilder();
//                data.append(PsiDebugUtil.psiToString(psiFile, false, true));

//        CopyPasteManager.getInstance().setContents(new StringSelection(data.toString()));
//                System.out.println(data);

                  UndoUtil.markPsiFileForUndo(psiFile);
                }
        );


//        final PsiClass targetClass = getTargetClass(editor, psiFile);
//        if (null != targetClass) {
//          process(project, psiFile, targetClass);
//        }
      }
    } else {
      final VirtualFile[] files = PlatformDataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext);
      if (null != files) {
        for (VirtualFile file : files) {
          if (file.isDirectory()) {
//            processDirectory(project, file);
          } else {
//            processFile(project, file);
          }
        }
      }
    }
  }

//  @Nullable
//  private PsiClass getTargetClass(Editor editor, PsiFile file) {
//    int offset = editor.getCaretModel().getOffset();
//    PsiElement element = file.findElementAt(offset);
//    if (element == null) {
//      return null;
//    }
//
//    return null;
////    final PsiClass target = PsiTreeUtil.getParentOfType(element, PsiClass.class);
////    return target instanceof SyntheticElement ? null : target;
//  }

//  private void processFile(Project project, VirtualFile file) {
//    if (StdFileTypes.JAVA.equals(file.getFileType())) {
//      final PsiManager psiManager = PsiManager.getInstance(project);
//      PsiJavaFile psiFile = (PsiJavaFile) psiManager.findFile(file);
//      if (psiFile != null) {
//        process(project, psiFile);
//      }
//    }
//  }

//  private void process(@NotNull final Project project, @NotNull final PsiJavaFile psiJavaFile) {
////    executeCommand(project, () -> getHandler().invoke(project, psiJavaFile));
//  }

//  private void process(@NotNull final Project project, @NotNull final PsiFile psiFile, @NotNull final PsiClass psiClass) {
//    NotificationGroup group = new NotificationGroup(Version.PLUGIN_NAME, NotificationDisplayType.STICKY_BALLOON, true);
//    Notification notification = group.createNotification(
//        "PsiClass",
//        psiClass.getQualifiedName(),
//        NotificationType.INFORMATION,
//        new NotificationListener.UrlOpeningListener(false)
//    );
//
//    Notifications.Bus.notify(notification, project);
////    executeCommand(project, () -> getHandler().invoke(project, psiFile, psiClass));
//  }

  private void executeCommand(final Project project, final Runnable action) {
    CommandProcessor.getInstance().executeCommand(project,
            () -> ApplicationManager.getApplication().runWriteAction(action), getCommandName(), null);
  }

  private String getCommandName() {
    String text = getTemplatePresentation().getText();
    return text == null ? "" : text;
  }
}
