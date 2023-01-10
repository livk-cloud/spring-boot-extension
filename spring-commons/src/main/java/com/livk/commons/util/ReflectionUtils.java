package com.livk.commons.util;

import com.livk.commons.function.FieldFunction;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * ReflectionUtils
 * </p>
 *
 * @author livk
 */
@Slf4j
@UtilityClass
public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

    /**
     * Gets field name.
     *
     * @param <T>      the type parameter
     * @param function the function
     * @return the field name
     */
    public <T> String getFieldName(FieldFunction<T> function) {
        try {
            Method method = function.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(function);
            String getter = serializedLambda.getImplMethodName();
            if (getter.startsWith("get")) {
                getter = getter.substring(3);
            } else if (getter.startsWith("is")) {
                getter = getter.substring(2);
            } else {
                throw new FileNotFoundException("缺少get|is方法");
            }
            return StringUtils.uncapitalize(getter);
        } catch (Exception e) {
            log.error("获取字段名称失败 message: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Sets field and accessible.
     *
     * @param field     the field
     * @param parameter the parameter
     * @param value     the value
     */
    public void setFieldAndAccessible(Field field, Object parameter, Object value) {
        field.setAccessible(true);
        setField(field, parameter, value);
    }

    /**
     * Gets read methods.
     *
     * @param targetClass the target class
     * @return the read methods
     */
    public Set<Method> getReadMethods(Class<?> targetClass) {
        Field[] fields = targetClass.getDeclaredFields();
        return Arrays.stream(fields)
                .map(field -> getReadMethod(targetClass, field))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Gets read method.
     *
     * @param targetClass the target class
     * @param field       the field
     * @return the read method
     */
    public Method getReadMethod(Class<?> targetClass, Field field) {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), targetClass);
            return descriptor.getReadMethod();
        } catch (Exception e) {
            log.error("获取字段get方法失败 message: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Gets all fields.
     *
     * @param targetClass the target class
     * @return the all fields
     */
    public List<Field> getAllFields(Class<?> targetClass) {
        List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = targetClass;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(allFields, declaredFields);
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

    /**
     * Gets declared field value.
     *
     * @param field  the field
     * @param target the target
     * @return the declared field value
     */
    public static Object getDeclaredFieldValue(Field field, Object target) {
        field.setAccessible(true);
        return ReflectionUtils.getField(field, target);
    }
}
