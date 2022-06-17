package com.livk.util;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * <p>
 * BeanUtils
 * </p>
 *
 * @author livk
 * @date 2022/6/7
 */
@UtilityClass
public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * 基于BeanUtils的复制
     *
     * @param source      目标源
     * @param targetClass 需复制的结果类型
     * @param <T>         类型
     * @return result
     */
    public <T> T copy(Object source, Class<T> targetClass) {
        return copy(source, () -> instantiateClass(targetClass));
    }

    /**
     * 基于BeanUtils的复制
     *
     * @param source   目标源
     * @param supplier 供应商
     * @param <T>      类型
     * @return result
     */
    public <T> T copy(Object source, Supplier<T> supplier) {
        if (supplier == null) {
            return null;
        }
        T t = supplier.get();
        if (source != null) {
            copyProperties(source, t);
        }
        return t;
    }

    /**
     * list类型复制
     *
     * @param sourceList  目标list
     * @param targetClass class类型
     * @param <T>         类型
     * @return result list
     */
    public <T> List<T> copyList(Collection<Object> sourceList, Class<T> targetClass) {
        return sourceList.stream().map(source -> copy(source, targetClass)).toList();
    }

}
