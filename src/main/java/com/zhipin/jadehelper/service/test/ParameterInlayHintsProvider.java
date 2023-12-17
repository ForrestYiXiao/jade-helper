package com.zhipin.jadehelper.service.test;

import com.intellij.codeInsight.hints.*;
import com.intellij.codeInsight.hints.presentation.InlayTextMetricsStorage;
import com.intellij.codeInsight.hints.presentation.TextInlayPresentation;
import com.intellij.lang.Language;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.*;

import javax.swing.*;
import java.awt.*;

public class ParameterInlayHintsProvider implements InlayHintsProvider<NoSettings> {

    @Override
    public boolean isLanguageSupported(Language language) {
        return language.is(JavaLanguage.INSTANCE);
    }

    @Override
    public InlayHintsCollector getCollectorFor(PsiFile file, Editor editor, NoSettings settings, InlayHintsSink sink) {
        return new InlayHintsCollector() {
            @Override
            public boolean collect(PsiElement element, Editor editor, InlayHintsSink sink) {
                if (element instanceof PsiMethod && element.getContainingFile().getName().endsWith("DAO.java")) {
                    PsiParameter[] parameters = ((PsiMethod) element).getParameterList().getParameters();
                    for (int index = 0; index < parameters.length; index++) {
                        String text = (index + 1) + ": ";
                        // 创建 TextAttributes 对象并设置前景色和背景色
                        TextAttributes textAttributes = new TextAttributes(Color.RED, Color.YELLOW, Color.BLUE, EffectType.BOLD_DOTTED_LINE, Font.PLAIN);
                        EditorImpl editorImpl = (EditorImpl) editor;
//                        editorImpl.setPlaceholderAttributes(textAttributes);
                        InlayTextMetricsStorage inlayTextMetricsStorage = new InlayTextMetricsStorage(editorImpl);
                        TextInlayPresentation presentation = new TextInlayPresentation(inlayTextMetricsStorage, false, text);
//                        presentation.paint((Graphics2D) editorImpl.getComponent().getGraphics(), textAttributes);
                        sink.addInlineElement(parameters[index].getTextOffset(), true, presentation, true);
                    }
                }
                return true;
            }
        };
    }

    @Override
    public NoSettings createSettings() {
        return new NoSettings();
    }

    @Override
    public String getName() {
        return "Parameter Index Hints";
    }

    @Override
    public InlayGroup getGroup() {
        return InlayGroup.PARAMETERS_GROUP;
    }

    @Override
    public SettingsKey<NoSettings> getKey() {
        return new SettingsKey<>("ParameterIndexHints");
    }

    @Override
    public String getPreviewText() {
        return "void method(Type parameter);";
    }

    @Override
    public ImmediateConfigurable createConfigurable(NoSettings settings) {
        return new ImmediateConfigurable() {
            @Override
            public JComponent createComponent(ChangeListener listener) {
                return new JLabel("Show indices before method parameters.");
            }
        };
    }

    @Override
    public boolean isVisibleInSettings() {
        return true;
    }
}

