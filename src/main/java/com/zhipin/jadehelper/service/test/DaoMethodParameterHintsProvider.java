package com.zhipin.jadehelper.service.test;

import com.intellij.codeInsight.hints.*;
import com.intellij.codeInsight.hints.presentation.InlayPresentation;
import com.intellij.codeInsight.hints.presentation.InlayTextMetricsStorage;
import com.intellij.codeInsight.hints.presentation.TextInlayPresentation;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.psi.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DaoMethodParameterHintsProvider implements InlayHintsProvider<NoSettings> {

    @NotNull
    @Override
    public InlayHintsCollector getCollectorFor(@NotNull PsiFile file, @NotNull Editor editor, @NotNull NoSettings settings, @NotNull InlayHintsSink sink) {
        if (!(file instanceof PsiJavaFile) || !file.getName().endsWith("DAO.java")) {
            return new InlayHintsCollector() {
                @Override
                public boolean collect(@NotNull PsiElement psiElement, @NotNull Editor editor, @NotNull InlayHintsSink inlayHintsSink) {
                    return false;
                }
            };
        }

        return new FactoryInlayHintsCollector(editor) {
            @Override
            public boolean collect(@NotNull PsiElement element, @NotNull Editor editor, @NotNull InlayHintsSink sink) {
                if (element instanceof PsiMethod) {
                    PsiParameter[] parameters = ((PsiMethod) element).getParameterList().getParameters();
                    for (int i = 0; i < parameters.length; i++) {
                        PsiParameter parameter = parameters[i];
                        int position = parameter.getTextOffset();
                        InlayTextMetricsStorage inlayTextMetricsStorage = new InlayTextMetricsStorage((EditorImpl) editor);
                        InlayPresentation presentation = new TextInlayPresentation(inlayTextMetricsStorage, false, (i + 1) + ": ");
                        sink.addInlineElement(position, true, presentation);
                    }
                    return true;
                } else if (element instanceof PsiJavaFile) {
                    List<PsiMethod> psiMethods = getAllMethodsFromPsiJavaFile((PsiJavaFile) element);
                    for (PsiMethod psiMethod : psiMethods) {
                        processPsiElement(psiMethod, editor, sink);
                    }
                    return true;
                }
                return false;
            }
        };
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


    private void processPsiElement(@NotNull PsiElement element, @NotNull Editor editor, @NotNull InlayHintsSink sink) {
        if (element instanceof PsiMethod) {
            PsiParameter[] parameters = ((PsiMethod) element).getParameterList().getParameters();
            for (int i = 0; i < parameters.length; i++) {
                PsiParameter parameter = parameters[i];
                int position = parameter.getTextOffset();
                InlayTextMetricsStorage inlayTextMetricsStorage = new InlayTextMetricsStorage((EditorImpl) editor);
                InlayPresentation presentation = new TextInlayPresentation(inlayTextMetricsStorage, true, (i + 1) + ": ");
                sink.addInlineElement(position, true, presentation);
            }
        }
        // Continue iterating over children
//        for (PsiElement child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
//            processPsiElement(child, editor, sink);
//        }
    }

    @NotNull
    @Override
    public NoSettings createSettings() {
        return new NoSettings();
    }

    @NotNull
    public String getCaseId() {
        return "DaoMethodParameterHintsProvider";
    }

    @NotNull
    @Override
    public SettingsKey<NoSettings> getKey() {
        return new SettingsKey<>(getCaseId());
    }

    @Override
    public ImmediateConfigurable createConfigurable(@NotNull NoSettings settings) {
        return settingsProvider -> null;
    }

    @Override
    public boolean isVisibleInSettings() {
        return false;
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getName() {
        return null;
    }

    @Nullable
    @Override
    public String getPreviewText() {
        return null;
    }

    @Override
    public boolean isLanguageSupported(@NotNull Language language) {
        if (Language.findLanguageByID("JAVA") == language) {
            return true;
        } else {
            return false;
        }
    }
}


