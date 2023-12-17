package com.zhipin.jadehelper.service.test;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;

public class MethodAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof PsiMethod && element.getContainingFile().getName().endsWith("DAO.java")) {
            PsiMethod method = (PsiMethod) element;
            PsiParameter[] parameters = method.getParameterList().getParameters();
            for (int i = 0; i < parameters.length; i++) {
                PsiParameter parameter = parameters[i];

                PsiType type = parameter.getType();
                String typeDescription = getHelperDesc(i, type);
                holder.newAnnotation(HighlightSeverity.INFORMATION, typeDescription)
                        .range(parameter)
                        .create();
                holder.newAnnotation(HighlightSeverity.INFORMATION, "Jade" + ": " + (i + 1))
                        .range(parameter)
                        .create();
//                Annotation annotation = holder.createInfoAnnotation(parameter, typeDescription);

//                TextAttributes textAttributes = new TextAttributes(Color.RED, Color.YELLOW, Color.BLUE, EffectType.BOLD_DOTTED_LINE, Font.PLAIN);
//                annotation.setEnforcedTextAttributes(textAttributes);
            }
        }
    }

    @NotNull
    private static String getHelperDesc(int i, PsiType type) {
        String typeDescription = "";
        String message = ":" + (i + 1);
        switch (type.getCanonicalText()) {
            case "int":
            case "float":
            case "double":
                typeDescription = " if( " + message + " >= 0){ do } ";
                break;
            case "java.lang.Integer":
            case "java.lang.Float":
            case "java.lang.Double":
                typeDescription = " if( " + message + " != null and " + message + " >= 0){ do } ";
                break;
            case "java.util.Date":
                typeDescription = " if( " + message + " != null ){ do } ";
                break;
            case "java.lang.String":
                typeDescription = " if( " + message + " != null and " + message + " != '' ){ do } ";
                break;
            // 添加其他case语句以处理其他类型
            default:
                typeDescription = " Unknown type: " + type.getCanonicalText();
                break;
        }
//        typeDescription = "Jade " + message + typeDescription;
        return typeDescription;
    }
}
