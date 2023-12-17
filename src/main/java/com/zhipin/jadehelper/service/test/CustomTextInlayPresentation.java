package com.zhipin.jadehelper.service.test;//package com.zhipin.bzlcode.service.test;
//
//import com.intellij.codeInsight.hints.presentation.InlayTextMetricsStorage;
//import com.intellij.codeInsight.hints.presentation.TextInlayPresentation;
//import com.intellij.openapi.editor.colors.EditorFontType;
//import com.intellij.openapi.editor.colors.TextAttributesKey;
//import com.intellij.openapi.editor.ex.util.EditorUtil;
//import com.intellij.openapi.editor.impl.EditorImpl;
//import com.intellij.ui.JBColor;
//import org.jetbrains.annotations.NotNull;
//
//import java.awt.*;
//
//public class CustomTextInlayPresentation extends TextInlayPresentation {
//    public CustomTextInlayPresentation(@NotNull InlayTextMetricsStorage metricsStorage, boolean isSmall, @NotNull String text) {
//        super(metricsStorage, isSmall, text);
//    }
////    private final String myText;
////    private final EditorImpl myEditor;
////    private final boolean myInline;
////
////    public CustomTextInlayPresentation(InlayTextMetricsStorage metricsStorage, boolean inline, String text, EditorImpl editor) {
////        super(metricsStorage, inline, text);
////        myText = text;
////        myEditor = editor;
////        myInline = inline;
////    }
////
////    @Override
////    protected void paint(@NotNull Graphics2D g, int offset, int baseline, boolean isSelected, int width, int height) {
////        Font originalFont = g.getFont();
////        Color originalColor = g.getColor();
////
////        // 设置字体和颜色
////        g.setFont(myEditor.getColorsScheme().getFont(EditorFontType.BOLD));
////        g.setColor(JBColor.BLUE);
////
////        int textWidth = EditorUtil.textWidth(myEditor, myText, myInline ? EditorFontType.PLAIN : EditorFontType.BOLD);
////        int textX = (width - textWidth) / 2;
////        g.drawString(myText, offset + textX, baseline);
////
////        // 恢复原始设置
////        g.setFont(originalFont);
////        g.setColor(originalColor);
////    }
//}
//
//
