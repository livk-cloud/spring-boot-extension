package com.livk.commons.function;

import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
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
     * Gets field name.
     *
     * @param <T>      the type parameter
     * @param function the function
     * @return the field name
     */
    @SneakyThrows
    static <T> String get(FieldFunc<T> function) {
        Method method = function.getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(true);
        SerializedLambda serializedLambda = (SerializedLambda) method.invoke(function);
        String getter = serializedLambda.getImplMethodName();
        if (getter.startsWith(GET_PREFIX)) {
            getter = getter.replaceFirst(GET_PREFIX, "");
        } else if (getter.startsWith(IS_PREFIX)) {
            getter = getter.replaceFirst(IS_PREFIX, "");
        } else {
            throw new NoSuchMethodException("缺少get|is方法");
        }
        return StringUtils.uncapitalize(getter);
    }
}
