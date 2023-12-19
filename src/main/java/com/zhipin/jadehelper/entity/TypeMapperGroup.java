package com.zhipin.jadehelper.entity;

import lombok.Data;

import java.util.List;

/**
 * 类型映射分组
 *
 * @author Zhang Yixiao
 * @version 1.0.0
 */
@Data
public class TypeMapperGroup implements AbstractGroup<TypeMapperGroup, TypeMapper> {
    /**
     * 分组名称
     */
    private String name;
    /**
     * 元素对象
     */
    private List<TypeMapper> elementList;
}
