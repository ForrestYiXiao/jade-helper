package com.zhipin.jadehelper.entity;

import lombok.Data;

/**
 * 调试字段实体类
 */
@Data
public class DebugField {
    /**
     * 字段名
     */
    private String name;
    /**
     * 字段类型
     */
    private Class<?> type;
    /**
     * 字段值
     */
    private String value;
}
