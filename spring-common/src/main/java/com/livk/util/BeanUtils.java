package com.livk.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

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
     * @param source 目标源
     * @param targetClass 需复制的结果类型
     * @param <T> 类型
     * @return result
     */
    @SneakyThrows
    public <T> T copy(Object source, Class<T> targetClass) {
        var t = instantiateClass(targetClass);
        copyProperties(source, t);
        return t;
    }

    /**
     * list类型复制
     * @param sourceList 目标list
     * @param targetClass class类型
     * @param <T> 类型
     * @return result list
     */
    public <T> List<T> copyList(Collection<Object> sourceList, Class<T> targetClass) {
        return sourceList.stream().map(source -> copy(source, targetClass)).toList();
    }
}
