package com.livk.commons.bean.util;

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
 */
@UtilityClass
public class ClassUtils extends org.springframework.util.ClassUtils {

    /**
     * To class class.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the class
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T> Class<T> toClass(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            return (Class<T>) parameterizedType.getRawType();
        } else if (type instanceof TypeVariable<?> typeVariable) {
            String className = typeVariable.getGenericDeclaration().toString();
            return (Class<T>) forName(className, Thread.currentThread().getContextClassLoader());
        } else {
            return (Class<T>) type;
        }
    }

}
