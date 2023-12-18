package com.zhipin.jadehelper.entity;

import com.zhipin.jadehelper.tool.CloneUtils;

/**
 * 抽象的项
 */
public interface AbstractItem<T extends AbstractItem> {
    /**
     * 默认值
     *
     * @return {@link T}
     */
    T defaultVal();

    /**
     * 克隆对象
     *
     * @return 克隆结果
     */
    @SuppressWarnings("unchecked")
    default T cloneObj() {
        return (T) CloneUtils.cloneByJson(this);
    }
}
