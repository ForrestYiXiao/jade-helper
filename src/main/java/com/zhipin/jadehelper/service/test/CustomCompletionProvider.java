package com.zhipin.jadehelper.service.test;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author Zhang Yixiao
 * @date 2023/10/27 14:16
 */
public class CustomCompletionProvider extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters,
                                  @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet resultSet) {
        // 在这里添加你的自定义提示
        resultSet.addElement(LookupElementBuilder.create("pageSize"));
    }
}
