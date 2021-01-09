// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.github.beansoftapp.javascript.codeInsight.daemon.impl;

import com.intellij.concurrency.JobLauncher;
import com.intellij.openapi.progress.EmptyProgressIndicator;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class to support Code Vision for java
 */
public final class JavaScriptTelescope {
  private static final int TOO_MANY_USAGES = -1;

  public static String usagesHint(@NotNull PsiElement member, @NotNull PsiFile file) {
    Project project = file.getProject();

    AtomicInteger totalUsageCount = new AtomicInteger();
    ProgressIndicator progress = ObjectUtils.notNull(ProgressIndicatorProvider.getGlobalProgressIndicator(),
        /*todo remove*/new EmptyProgressIndicator());
    List<PsiElement> things =
      Collections.singletonList(member);
    JobLauncher.getInstance().invokeConcurrentlyUnderProgress(things, progress, e -> {
      int count = usagesCount(project, file, e, progress);
      int newCount = totalUsageCount.updateAndGet(old -> count == TOO_MANY_USAGES ? TOO_MANY_USAGES : old + count);
      return newCount != TOO_MANY_USAGES;
    });
    if (totalUsageCount.get() == TOO_MANY_USAGES || totalUsageCount.get() == 0) return null;
    String format = "{0,choice, 0#no usages|1#1 usage|2#{0,number} usages}";
    return MessageFormat.format(format, totalUsageCount.get());
  }

  private static int usagesCount(@NotNull Project project,
                                 @NotNull PsiFile containingFile,
                                 @NotNull final PsiElement member,
                                 @NotNull ProgressIndicator progress) {
//    AtomicInteger count = new AtomicInteger();
//    boolean ok = JavaScriptUnusedSymbolUtil.processUsages(project, containingFile, member, progress, null, info -> {
//      PsiFile psiFile = info.getFile();
//      if (psiFile == null) {
//        return true;
//      }
//      int offset = info.getNavigationOffset();
//      if (offset == -1) return true;
//      count.incrementAndGet();
//      return true;
//    });
//    if (!ok) {
//      return TOO_MANY_USAGES;
//    }
//    return count.get();

    Collection<PsiElement> psiElementCollection = JavaScriptUnusedSymbolUtil.findElementsToHighlight(project, member, containingFile);

    return psiElementCollection.size();
  }


}
