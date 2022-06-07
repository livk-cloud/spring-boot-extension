package com.livk.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * <p>
 * ClassUtils
 * </p>
 *
 * @author livk
 * @date 2022/6/7
 */
@UtilityClass
public class ClassUtils extends org.springframework.util.ClassUtils {

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T> Class<T> toClass(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            return (Class<T>) parameterizedType.getRawType();
        } else if (type instanceof TypeVariable<?> typeVariable) {
            String className = typeVariable.getGenericDeclaration().toString();
            return (Class<T>) Class.forName(className);
        } else {
            return (Class<T>) type;
        }
    }
}
