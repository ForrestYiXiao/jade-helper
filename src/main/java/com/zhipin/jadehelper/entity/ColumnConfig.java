package com.zhipin.jadehelper.entity;

import com.zhipin.jadehelper.enums.ColumnConfigType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 列配置信息
 */
@Data
@NoArgsConstructor
public class ColumnConfig implements AbstractItem<ColumnConfig> {
    /**
     * 标题
     */
    private String title;
    /**
     * 类型
     */
    private ColumnConfigType type;
    /**
     * 可选值，逗号分割
     */
    private String selectValue;

    public ColumnConfig(String title, ColumnConfigType type) {
        this.title = title;
        this.type = type;
    }

    public ColumnConfig(String title, ColumnConfigType type, String selectValue) {
        this.title = title;
        this.type = type;
        this.selectValue = selectValue;
    }

    @Override
    public ColumnConfig defaultVal() {
        return new ColumnConfig("demo", ColumnConfigType.TEXT);
    }
}
