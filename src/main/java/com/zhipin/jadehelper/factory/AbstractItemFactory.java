package com.zhipin.jadehelper.factory;

import com.zhipin.jadehelper.entity.AbstractItem;

import java.lang.reflect.InvocationTargetException;

/**
 * 抽象的项目工厂
 *
 * @author Zhang Yixiao
 * @version 1.0.0
 */
public class AbstractItemFactory {

    public static <T extends AbstractItem<T>> T createDefaultVal(Class<T> cls) {
        try {
            T instance = cls.getDeclaredConstructor().newInstance();
            return instance.defaultVal();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalArgumentException("构建示例失败", e);
        }
    }

}
