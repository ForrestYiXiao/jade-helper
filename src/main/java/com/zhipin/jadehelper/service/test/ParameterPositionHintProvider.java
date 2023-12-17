package com.zhipin.jadehelper.service.test;

import com.intellij.codeInsight.hints.*;
import com.intellij.codeInsight.hints.presentation.InlayTextMetricsStorage;
import com.intellij.codeInsight.hints.presentation.TextInlayPresentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.psi.*;
import com.zhipin.jadehelper.service.test.CustomTextInlayDecorator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParameterPositionHintProvider implements InlayHintsProvider<NoSettings> {
    @NotNull
    @Override
    public InlayHintsCollector getCollectorFor(@NotNull PsiFile file, @NotNull Editor editor, @NotNull NoSettings settings, @NotNull InlayHintsSink sink) {
        return new InlayHintsCollector() {
            @Override
            public boolean collect(@NotNull PsiElement psiElement, @NotNull Editor editor, @NotNull InlayHintsSink inlayHintsSink) {
                if (psiElement instanceof PsiJavaFile) {
                    List<PsiMethod> psiMethods = getAllMethodsFromPsiJavaFile((PsiJavaFile) psiElement);
                    for (PsiMethod psiMethod : psiMethods) {
                        processPsiElement(psiMethod, editor, sink);
                    }
                    return true;
                }
                return false;
            }
        };
    }

    private void processPsiElement(@NotNull PsiElement element, @NotNull Editor editor, @NotNull InlayHintsSink sink) {
        if (element instanceof PsiMethod) {
            PsiParameter[] parameters = ((PsiMethod) element).getParameterList().getParameters();
            for (int i = 0; i < parameters.length; i++) {
                PsiParameter parameter = parameters[i];
                int position = parameter.getTextOffset();

                InlayTextMetricsStorage inlayTextMetricsStorage = new InlayTextMetricsStorage((EditorImpl) editor);
                TextInlayPresentation presentation = new TextInlayPresentation(inlayTextMetricsStorage, false, (i + 1) + ": ");
                CustomTextInlayDecorator customPresentation = new CustomTextInlayDecorator(presentation);
//                sink.addInlineElement(position, true, customPresentation)
//                InlayPresentation presentation = new TextInlayPresentation(inlayTextMetricsStorage, true, (i + 1) + ": ");
                sink.addInlineElement(position, false, customPresentation);

//                sink.addBlockElement(position, false, customPresentation, new BlockConstraints(true, 1););
            }
        }
    }

    public List<PsiMethod> getAllMethodsFromPsiJavaFile(PsiJavaFile psiJavaFile) {
        List<PsiMethod> allMethods = new ArrayList<>();

        // 获取PsiJavaFile中的所有顶级类
        PsiClass[] psiClasses = psiJavaFile.getClasses();
        for (PsiClass psiClass : psiClasses) {
            // 获取类中的所有方法
            PsiMethod[] psiMethods = psiClass.getMethods();
            Collections.addAll(allMethods, psiMethods);

            // 如果需要获取内部类的方法，可以再次使用类似的循环遍历
        }

        return allMethods;
    }

    @NotNull
    @Override
    public NoSettings createSettings() {
        return new NoSettings();
    }

    @NotNull
    @Override
    public String getName() {
        return "Parameter Position Hints";
    }

    @NotNull
    @Override
    public SettingsKey<NoSettings> getKey() {
        return new SettingsKey<>("parameter-position-hints");
    }

    @Nullable
    @Override
    public String getPreviewText() {
        return "int get(int id, int age, String name);";
    }

    @NotNull
    @Override
    public ImmediateConfigurable createConfigurable(@NotNull NoSettings settings) {
        return new ImmediateConfigurable() {
            @NotNull
            @Override
            public javax.swing.JComponent createComponent(@NotNull ChangeListener changeListener) {
                return new javax.swing.JPanel();
            }
        };
    }

    @Override
    public boolean isLanguageSupported(@NotNull com.intellij.lang.Language language) {
        return "JAVA".equals(language.getID());
    }

    @Override
    public boolean isVisibleInSettings() {
        return true;
    }
}
