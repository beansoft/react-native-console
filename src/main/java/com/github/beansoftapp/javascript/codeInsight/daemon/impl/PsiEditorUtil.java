package com.github.beansoftapp.javascript.codeInsight.daemon.impl;

import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import org.jetbrains.annotations.NotNull;

public class PsiEditorUtil {
  public static int getAnchorOffset(@NotNull PsiElement element) {
    for (PsiElement child : element.getChildren()) {
      if (!(child instanceof PsiComment) && !(child instanceof PsiWhiteSpace)) {
        return child.getTextRange().getStartOffset();
      }
    }
    return element.getTextRange().getStartOffset();
  }
}
