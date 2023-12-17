package com.zhipin.jadehelper.service.test;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class MethodParameterIntention implements IntentionAction {

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getText() {
        return "Add parameter indices";
    }

    @Override
    public @NotNull String getFamilyName() {
        return getText();
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        if (!(file instanceof PsiJavaFile)) return false;
        return file.getName().endsWith("DAO.java");
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        ///* 1: */Date startTime, /* 2: */Date endTime)
        file.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethod(PsiMethod method) {
                super.visitMethod(method);
                PsiParameter[] parameters = method.getParameterList().getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    PsiParameter parameter = parameters[i];
                    PsiElement typeElement = parameter.getTypeElement();
                    if (typeElement != null) {
                        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
                        PsiComment addedElement = elementFactory.createCommentFromText("/* " + (i + 1) + ": */", null);
                        method.getParameterList().addBefore(addedElement, parameter);
                    }
                }
            }
        });
    }


    @Override
    public boolean startInWriteAction() {
        return true;
    }
}
