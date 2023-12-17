package com.zhipin.jadehelper.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.zhipin.jadehelper.dict.GlobalDict;
import com.zhipin.jadehelper.dto.SettingsStorageDTO;
import com.zhipin.jadehelper.tool.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

/**
 * @author makejava
 * @version 1.0.0
 * @date 2021/08/07 09:22
 */
public class MainSettingForm implements Configurable, Configurable.Composite, BaseSettings {
    private JLabel versionLabel;
    private JPanel mainPanel;
    private JTextField authorEditor;
    private JLabel authorTitle;

    /**
     * 子配置
     */
    private Configurable[] childConfigurableArray;

    public MainSettingForm() {
    }

    @Override
    public String getDisplayName() {
        return "JadeHelper";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return getDisplayName();
    }

    @Override
    public Configurable @NotNull [] getConfigurables() {
        this.childConfigurableArray = new Configurable[]{
                new TypeMapperSettingForm(),
                new TemplateSettingForm(),
                new ColumnConfigSettingForm(),
                new GlobalConfigSettingForm(),
        };
        this.loadChildSettingsStore();
        return this.childConfigurableArray;
    }

    private void loadChildSettingsStore() {
        // 初始装置配置信息
        for (Configurable configurable : this.childConfigurableArray) {
            if (configurable instanceof BaseSettings) {
                ((BaseSettings) configurable).loadSettingsStore();
            }
        }
    }

    @Override
    public @Nullable JComponent createComponent() {
        // 初始图标
        this.authorTitle.setIcon(AllIcons.Actions.IntentionBulb);
        // 加载储存数据
        this.loadSettingsStore();
        // 初始化事件
        return mainPanel;
    }

    @Override
    public boolean isModified() {
        if (!Objects.equals(this.authorEditor.getText(), getSettingsStorage().getAuthor())) {
            return true;
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        String author = this.authorEditor.getText();
        if (StringUtils.isEmpty(author)) {
            throw new ConfigurationException("作者名称不能为空");
        }
        getSettingsStorage().setAuthor(author);
    }

    /**
     * 加载配置信息
     *
     * @param settingsStorage 配置信息
     */
    @Override
    public void loadSettingsStore(SettingsStorageDTO settingsStorage) {
        this.versionLabel.setText(GlobalDict.VERSION);
        this.authorEditor.setText(settingsStorage.getAuthor());
    }
}
