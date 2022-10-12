package com.livk.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

/**
 * <p>
 * ReflectionUtils
 * </p>
 *
 * @author livk
 * @date 2022/10/12
 */
@UtilityClass
public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

    public void set(Field field, Object parameter, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(parameter, value);
    }

}
