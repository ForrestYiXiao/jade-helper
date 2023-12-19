package com.zhipin.jadehelper.service.test;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author Zhang Yixiao
 */
public class CustomCompletionContributor extends CompletionContributor {
    public CustomCompletionContributor() {
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().inside(PsiMethodCallExpression.class),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        resultSet.addElement(LookupElementBuilder.create("pageSize"));
                    }
                });
    }
}
