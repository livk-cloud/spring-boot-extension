package com.livk.commons.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * <p>
 * BeanUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * 基于BeanUtils的复制
     *
     * @param <T>         类型
     * @param source      目标源
     * @param targetClass 需复制的结果类型
     * @return result t
     */
    public <T> T copy(Object source, Class<T> targetClass) {
        return copy(source, () -> instantiateClass(targetClass));
    }

    /**
     * 基于BeanUtils的复制
     *
     * @param <T>      类型
     * @param source   目标源
     * @param supplier 供应商
     * @return result t
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
     * @param <T>         类型
     * @param sourceList  目标list
     * @param targetClass class类型
     * @return result list
     */
    public <T> List<T> copyList(Collection<?> sourceList, Class<T> targetClass) {
        return sourceList.stream().map(source -> copy(source, targetClass)).toList();
    }

    /**
     * Is field null boolean.
     *
     * @param source the source
     * @return the boolean
     */
    public static boolean isFieldNull(Object source) {
        if (source == null) {
            return true;
        }
        return Arrays.stream(source.getClass().getDeclaredFields())
                .noneMatch(field -> {
                    field.setAccessible(true);
                    try {
                        return field.get(source) != null;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
