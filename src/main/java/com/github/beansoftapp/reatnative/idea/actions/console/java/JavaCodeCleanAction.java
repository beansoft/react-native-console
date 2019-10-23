package com.github.beansoftapp.reatnative.idea.actions.console.java;

import com.github.beansoftapp.reatnative.idea.utils.FileUtil;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.openapi.vfs.newvfs.ArchiveFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.helper.StringUtil;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JavaCodeCleanAction extends DumbAwareAction {
  private static final Logger LOG = Logger.getInstance(JavaCodeCleanAction.class);

  @Override
  public void update(@NotNull AnActionEvent e) {
    e.getPresentation().setEnabledAndVisible(getFile(e) != null);
  }


  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    VirtualFile file = getFile(e);
    if (file != null) {
      openFile(new File(file.getPresentableUrl()));
    }
  }

  /**
   * Opens a system file manager with given file's parent directory open and the file highlighted in it
   * (note that not all platforms support highlighting).
   */
  public static void openFile(@NotNull File file) {
    if (!file.exists()) {
      LOG.info("does not exist: " + file);
      return;
    }

    try {
      String content = cleanJava(file);
      if (!StringUtil.isBlank(content)) {
        FileUtil.writeFileString(file.getAbsolutePath(), content);
      }
    } catch (Exception e) {
      e.printStackTrace();

      NotificationUtils.errorNotification("Clean failed:" + e.getMessage());
    }
  }

  static String cleanJava(File in) throws Exception {
    ParseResult<CompilationUnit> result = new JavaParser().parse(in);
    CompilationUnit cu = null;
    if (result.isSuccessful()) {
      Optional<CompilationUnit> optionalCompilationUnit = result.getResult();
      if (optionalCompilationUnit.isPresent()) {
        cu = optionalCompilationUnit.get();
      }
    }

    if (cu == null) {
      System.err.println("Parse failure, return.");
      return null;
    }

    List<Comment> comments = cu.getAllContainedComments();
    List<Comment> unwantedComments = comments
        .stream()
        .filter(p -> !p.getCommentedNode().isPresent() || p instanceof LineComment || p instanceof JavadocComment)
        .collect(Collectors.toList());

    if (unwantedComments.size() > 0) {
      System.err.println(unwantedComments.get(0).toString());
      if (unwantedComments.get(0).toString().contains("@author")) {
        unwantedComments.remove(0);
      }
    }

    unwantedComments.forEach(Node::remove);


    return cu.toString();
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

  @Nullable
  private static VirtualFile getFile(@NotNull AnActionEvent e) {
    return findLocalFile(e.getData(CommonDataKeys.VIRTUAL_FILE));
  }


}
