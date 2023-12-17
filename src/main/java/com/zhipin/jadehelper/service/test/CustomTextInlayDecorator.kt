package com.zhipin.jadehelper.service.test

import com.intellij.codeInsight.hints.presentation.InlayPresentation
import com.intellij.codeInsight.hints.presentation.TextInlayPresentation
import com.intellij.openapi.editor.markup.EffectType
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.ui.JBColor
import java.awt.Font
import java.awt.Graphics2D

/**
 * @author Zhang Yixiao
 * @date 2023/10/27 21:12
 */
class CustomTextInlayDecorator(private val innerPresentation: TextInlayPresentation) : InlayPresentation by innerPresentation {
    override fun paint(g: Graphics2D, attributes: TextAttributes) {
        val customAttributes = TextAttributes(attributes.foregroundColor, attributes.backgroundColor, JBColor.BLUE, EffectType.BOLD_DOTTED_LINE, Font.BOLD)
        innerPresentation.paint(g, customAttributes)
    }
}

