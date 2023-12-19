package com.zhipin.jadehelper.entity;

import lombok.Data;

import java.util.List;

/**
 * 全局配置分组
 *
 * @author Zhang Yixiao
 * @version 1.0.0
 */
@Data
public class GlobalConfigGroup implements AbstractGroup<GlobalConfigGroup, GlobalConfig> {
    /**
     * 分组名称
     */
    private String name;
    /**
     * 元素对象集合
     */
    private List<GlobalConfig> elementList;
}
