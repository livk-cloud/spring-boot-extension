package com.livk.commons.function;

import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * The interface Field function.
 *
 * @param <T> the type parameter
 * @author livk
 */
@FunctionalInterface
public interface FieldFunc<T> extends Function<T, Object>, Serializable {

    /**
     * The constant GET_PREFIX.
     */
    String GET_PREFIX = "get";

    /**
     * The constant IS_PREFIX.
     */
    String IS_PREFIX = "is";

    /**
     * Serialized lambda serialized lambda.
     *
     * @param <T>      the type parameter
     * @param function the function
     * @return the serialized lambda
     */
    @SneakyThrows
    private static <T> SerializedLambda serializedLambda(FieldFunc<T> function) {
        Method method = function.getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(true);
        return (SerializedLambda) method.invoke(function);
    }

    /**
     * Gets field name.
     *
     * @param <T>      the type parameter
     * @param function the function
     * @return the field name
     */
    @SneakyThrows
    static <T> String getName(FieldFunc<T> function) {
        SerializedLambda serializedLambda = serializedLambda(function);
        String methodName = serializedLambda.getImplMethodName();
        if (methodName.startsWith(GET_PREFIX)) {
            methodName = methodName.replaceFirst(GET_PREFIX, "");
        } else if (methodName.startsWith(IS_PREFIX)) {
            methodName = methodName.replaceFirst(IS_PREFIX, "");
        }
        return StringUtils.uncapitalize(methodName);
    }

    /**
     * Gets field.
     *
     * @param <T>      the type parameter
     * @param function the function
     * @return the field
     */
    @SneakyThrows
    static <T> Field get(FieldFunc<T> function) {
        SerializedLambda serializedLambda = serializedLambda(function);
        String className = StringUtils.replace(serializedLambda.getImplClass(), "/", ".");
        Class<?> type = Class.forName(className);
        return type.getDeclaredField(getName(function));
    }
}
