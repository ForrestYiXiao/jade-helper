package com.zhipin.jadehelper.ui.base;

import com.intellij.openapi.ui.InputValidator;
import com.zhipin.jadehelper.tool.StringUtils;

import java.util.Collection;

/**
 * 输入存在验证器
 *
 * @author Zhang Yixiao
 * @version 1.0.0
 */
public class InputExistsValidator implements InputValidator {

    private Collection<String> itemList;

    public InputExistsValidator(Collection<String> itemList) {
        this.itemList = itemList;
    }

    @Override
    public boolean checkInput(String inputString) {
        return !StringUtils.isEmpty(inputString) && !itemList.contains(inputString);
    }

    @Override
    public boolean canClose(String inputString) {
        return this.checkInput(inputString);
    }
}
