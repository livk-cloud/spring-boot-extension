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
        if (getter.startsWith("get")) {
            getter = getter.substring(3);
        } else if (getter.startsWith("is")) {
            getter = getter.substring(2);
        } else {
            throw new NoSuchMethodException("缺少get|is方法");
        }
        return StringUtils.uncapitalize(getter);
    }
}
