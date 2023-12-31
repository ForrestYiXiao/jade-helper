package com.zhipin.jadehelper.service;


import com.zhipin.jadehelper.dto.SettingsStorageDTO;

/**
 * 导出导入设置服务
 *
 * @author Zhang Yixiao
 * @version 1.0.0
 */
public interface ExportImportSettingsService {

    /**
     * 导出设置
     *
     * @param settingsStorage 要导出的设置
     */
    void exportConfig(SettingsStorageDTO settingsStorage);

    /**
     * 导入设置
     *
     * @return 设置信息
     */
    SettingsStorageDTO importConfig();

}
