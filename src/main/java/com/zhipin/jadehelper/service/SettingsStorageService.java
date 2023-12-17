package com.zhipin.jadehelper.service;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.zhipin.jadehelper.dto.SettingsStorageDTO;
import com.zhipin.jadehelper.service.impl.SettingsStorageServiceImpl;

/**
 * @author Zhang Yixiao
 * @version 1.0.0
 * @date 2021/08/07 11:55
 */
public interface SettingsStorageService extends PersistentStateComponent<SettingsStorageDTO> {
    /**
     * 获取实例
     *
     * @return {@link SettingsStorageService}
     */
    static SettingsStorageService getInstance() {
        return ServiceManager.getService(SettingsStorageServiceImpl.class);
    }

    /**
     * 获取设置存储
     *
     * @return {@link SettingsStorageDTO}
     */
    static SettingsStorageDTO getSettingsStorage() {
        return getInstance().getState();
    }
}
