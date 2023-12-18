package com.zhipin.jadehelper.service.impl;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.zhipin.jadehelper.dto.SettingsStorageDTO;
import com.zhipin.jadehelper.service.SettingsStorageService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 设置储存服务实现
 *
 * @author Zhang Yixiao
 * @version 1.0.0
 * @date 2021/08/07 11:32
 */
@State(name = "JadeHelperSetting", storages = @Storage("jade-helper-setting.xml"))
public class SettingsStorageServiceImpl implements SettingsStorageService {

    private SettingsStorageDTO settingsStorage = SettingsStorageDTO.defaultVal();

    /**
     * 获取配置
     *
     * @return 配置对象
     */
    @Nullable
    @Override
    public SettingsStorageDTO getState() {
        return settingsStorage;
    }

    /**
     * 加载配置
     *
     * @param state 配置对象
     */
    @Override
    public void loadState(@NotNull SettingsStorageDTO state) {
        // 加载配置后填充默认值，避免版本升级导致的配置信息不完善问题
        state.fillDefaultVal();
        this.settingsStorage = state;
    }
}
