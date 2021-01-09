// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.github.beansoftapp.javascript.codeInsight.daemon.impl;

import com.google.common.collect.Lists;
import com.intellij.find.findUsages.FindUsagesHelper;
import com.intellij.find.findUsages.FindUsagesOptions;
import com.intellij.lang.ecmascript6.psi.ES6ClassExpression;
import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.lang.javascript.psi.ecma6.impl.ES6FieldImpl;
import com.intellij.lang.javascript.psi.ecmal4.JSClass;
import com.intellij.lang.javascript.psi.ecmal4.JSQualifiedNamedElement;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiSearchHelper;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.Processor;
import com.intellij.util.Query;
import com.intellij.util.SmartList;
import com.intellij.util.containers.ContainerUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

public final class JavaScriptUnusedSymbolUtil {

    private static List<String> ignoreFieldNames = Lists.newArrayList("state");

    public static boolean isEmptyOrIgnoreName(@NotNull PsiElement element) {
        try {
            Method method = element.getClass().getMethod("getName");
            String name = (String) method.invoke(element);
            boolean isEmpty = StringUtils.isEmpty(name);
            if (!isEmpty && element instanceof ES6FieldImpl) {
                if (ignoreFieldNames.contains(name)) {
                    return true;
                }
            }

            return isEmpty;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    private static void log(@NonNls String s) {
        System.out.println(s);
    }

    public static Collection<PsiElement> findElementsToHighlight(@NotNull Project project, @NotNull PsiElement member,
                                                                 @NotNull PsiFile file) {
        SearchScope useScope = member.getUseScope();
        if (useScope instanceof GlobalSearchScope) {
            // Reduce the search range to avoid slow search progress
            if (member instanceof ES6ClassExpression) {
                useScope = GlobalSearchScope.projectScope(project);//.uniteWith((GlobalSearchScope) useScope);
            } else {
                // Only find in current file
                useScope = GlobalSearchScope.fileScope(project, file.getVirtualFile());
            }
        }
        Query<PsiReference> references = ReferencesSearch.search(member, useScope, false);
        return ContainerUtil.mapNotNull(references, psiReference -> psiReference.getElement());
    }

    // return false if can't process usages (weird member of too may usages) or processor returned false
    // TODO Not finished yet, can found nothing
    public static boolean processUsages(@NotNull Project project,
                                        @NotNull PsiFile containingFile,
                                        @NotNull JSQualifiedNamedElement member,
                                        @NotNull ProgressIndicator progress,
                                        @Nullable PsiFile ignoreFile,
                                        @NotNull Processor<? super UsageInfo> usageInfoProcessor) {
        String name = member.getName();
        if (name == null) {
            log("* " + member.getName() + " no name; false");
            return false;
        }
        SearchScope useScope = member.getUseScope();
        PsiSearchHelper searchHelper = PsiSearchHelper.getInstance(project);
        if (useScope instanceof GlobalSearchScope) {
            // some classes may have references from within XML outside dependent modules, e.g. our actions
            if (member instanceof JSClass) {
                useScope = GlobalSearchScope.projectScope(project);//.uniteWith((GlobalSearchScope) useScope);
            }

            PsiSearchHelper.SearchCostResult cheapEnough = searchHelper.isCheapEnoughToSearch(name, (GlobalSearchScope) useScope, ignoreFile, progress);
//      if (cheapEnough == PsiSearchHelper.SearchCostResult.TOO_MANY_OCCURRENCES
//        // try to search for private and package-private members unconditionally - they are unlikely to have millions of usages
//      ) {
//        log("* " + member.getName() + " too many usages; false");
//        return false;
//      }

            //search usages if it cheap
            //if count is 0 there is no usages since we've called myRefCountHolder.isReferenced() before
            if (cheapEnough == PsiSearchHelper.SearchCostResult.ZERO_OCCURRENCES && !canBeReferencedViaWeirdNames(member, containingFile)) {
                log("* " + member.getName() + " 0 usages; true");
                return true;
            }

//      if (member instanceof PsiMethod && member.hasModifierProperty(PsiModifier.PUBLIC)) {
//        String propertyName = PropertyUtilBase.getPropertyName(member);
//        if (propertyName != null) {
//          SearchScope fileScope = containingFile.getUseScope();
//          if (fileScope instanceof GlobalSearchScope &&
//              searchHelper.isCheapEnoughToSearch(propertyName, (GlobalSearchScope)fileScope, ignoreFile, progress) ==
//              PsiSearchHelper.SearchCostResult.TOO_MANY_OCCURRENCES) {
//            log("* "+member.getName()+" too many prop usages; false");
//            return false;
//          }
//        }
//      }
        }
        FindUsagesOptions options;
        Collection<JSQualifiedNamedElement> toSearch = new SmartList<>(member);
//    if (member instanceof PsiPackage) {
//      options = new JavaPackageFindUsagesOptions(useScope);
//      options.isSearchForTextOccurrences = true;
//    }
//    else if (member instanceof PsiClass) {
//      options = new JavaClassFindUsagesOptions(useScope);
//      options.isSearchForTextOccurrences = true;
//    }
//    else if (member instanceof PsiMethod) {
//      PsiMethod method = (PsiMethod)member;
//      options = new JavaMethodFindUsagesOptions(useScope);
//      options.isSearchForTextOccurrences = method.isConstructor();
//      toSearch.addAll(DeepestSuperMethodsSearch.search(method).findAll());
//    }
//    else if (member instanceof PsiVariable) {
//      options = new JavaVariableFindUsagesOptions(useScope);
//      options.isSearchForTextOccurrences = false;
//    }
//    else {
        options = new FindUsagesOptions(useScope);
        options.isSearchForTextOccurrences = true;
//    }
        options.isUsages = true;
        SearchScope finalUseScope = useScope;

        if (finalUseScope instanceof GlobalSearchScope) {
            Collection<String> stringsToSearch = Lists.newArrayList(name);
            log("Search for" + name);
            // todo add to fastTrack
            return ContainerUtil.process(toSearch, m -> FindUsagesHelper.processUsagesInText(
                    m, stringsToSearch, false, (GlobalSearchScope) finalUseScope, usageInfoProcessor));
        }

        return true;
    }

    private static boolean canBeReferencedViaWeirdNames(@NotNull JSQualifiedNamedElement member, @NotNull PsiFile containingFile) {
        if (member instanceof JSClass) return false;
        if (!(containingFile instanceof JavaScriptFileType))
            return true;  // Groovy field can be referenced from Java by getter
//    if (member instanceof PsiField) return false;  //Java field cannot be referenced by anything but its name
//    if (member instanceof PsiMethod) {
//      return PropertyUtilBase.isSimplePropertyAccessor((PsiMethod)member);  //Java accessors can be referenced by field name from Groovy
//    }
        return false;
    }
}




