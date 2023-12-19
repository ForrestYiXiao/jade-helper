package com.zhipin.jadehelper.service.impl;

import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.ui.TextTransferable;
import com.zhipin.jadehelper.dict.GlobalDict;
import com.zhipin.jadehelper.dto.SettingsStorageDTO;
import com.zhipin.jadehelper.service.ExportImportSettingsService;
import com.zhipin.jadehelper.tool.JSON;

import java.awt.datatransfer.DataFlavor;

/**
 * 剪切板导入导出配置服务实现
 *
 * @author Zhang Yixiao
 * @version 1.0.0
 */
public class ClipboardExportImportSettingsServiceImpl implements ExportImportSettingsService {
    /**
     * 导出设置
     *
     * @param settingsStorage 要导出的设置
     */
    @Override
    public void exportConfig(SettingsStorageDTO settingsStorage) {
        String json = JSON.toJsonByFormat(settingsStorage);
        CopyPasteManager.getInstance().setContents(new TextTransferable(json));
        Messages.showInfoMessage("Config info success write to clipboard！", GlobalDict.TITLE_INFO);
    }

    /**
     * 导入设置
     *
     * @return 设置信息
     */
    @Override
    public SettingsStorageDTO importConfig() {
        String json = CopyPasteManager.getInstance().getContents(DataFlavor.stringFlavor);
        return JSON.parse(json, SettingsStorageDTO.class);
    }
}
