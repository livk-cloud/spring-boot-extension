package com.livk.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;

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

    public Field[] getFields(Object parameter) {
        Field[] declaredFields = parameter.getClass().getDeclaredFields();
        if (parameter.getClass().getSuperclass() != null) {
            Field[] superFiled = parameter.getClass().getSuperclass().getDeclaredFields();
            declaredFields = ArrayUtils.addAll(declaredFields, superFiled);
        }
        return declaredFields;
    }

    public void set(Field field, Object parameter, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(parameter, value);
    }

}
