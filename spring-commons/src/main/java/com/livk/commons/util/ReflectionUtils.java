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

    public void setFieldAndAccessible(Field field, Object parameter, Object value) {
        field.setAccessible(true);
        setField(field, parameter, value);
    }

    public Set<Method> getReadMethods(Class<?> targetClass) {
        Field[] fields = targetClass.getDeclaredFields();
        return Arrays.stream(fields)
                .map(field -> getReadMethod(targetClass, field))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Method getReadMethod(Class<?> targetClass, Field field) {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), targetClass);
            return descriptor.getReadMethod();
        } catch (Exception e) {
            log.error("获取字段get方法失败 message: {}", e.getMessage(), e);
            return null;
        }
    }

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
}
