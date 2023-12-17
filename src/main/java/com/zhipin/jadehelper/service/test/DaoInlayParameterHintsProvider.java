package com.zhipin.jadehelper.service.test;

import com.intellij.codeInsight.hints.HintInfo;
import com.intellij.codeInsight.hints.InlayInfo;
import com.intellij.codeInsight.hints.InlayParameterHintsProvider;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DaoInlayParameterHintsProvider implements InlayParameterHintsProvider {

    @NotNull
    @Override
    public List<InlayInfo> getParameterHints(@NotNull PsiElement element) {
        List<InlayInfo> hints = new ArrayList<>();

        if (!(element instanceof PsiMethodCallExpression)) {
            return hints;
        }

        PsiMethodCallExpression methodCall = (PsiMethodCallExpression) element;
        PsiReferenceExpression methodExpression = methodCall.getMethodExpression();
        PsiMethod method = methodCall.resolveMethod();

        if (method == null) {
            return hints;
        }

        PsiFile containingFile = method.getContainingFile();
        if (!(containingFile instanceof PsiJavaFile) || !containingFile.getName().endsWith("DAO.java")) {
            return hints;
        }

        PsiExpression[] arguments = methodCall.getArgumentList().getExpressions();
        for (int i = 0; i < arguments.length; i++) {
            PsiExpression argument = arguments[i];
            hints.add(new InlayInfo((i + 1) + ": ", argument.getTextRange().getStartOffset()));
        }

        return hints;
    }

    @Override
    public @Nullable HintInfo getHintInfo(@NotNull PsiElement element) {
        return InlayParameterHintsProvider.super.getHintInfo(element);
    }

    @NotNull
    @Override
    public Set<String> getDefaultBlackList() {
        return Set.of(); // Return an empty blacklist, as we want to show hints for all methods
    }

    @Override
    public boolean isBlackListSupported() {
        return false; // We are not supporting a blacklist for this provider
    }

    // ... other methods from InlayParameterHintsProvider can be implemented as needed
}
