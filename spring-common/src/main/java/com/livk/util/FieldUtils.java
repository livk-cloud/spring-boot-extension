package com.livk.util;

import com.livk.function.FieldFunction;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>
 * ReflectionUtils
 * </p>
 *
 * @author livk
 * @date 2022/8/16
 */
@UtilityClass
public class FieldUtils extends org.apache.commons.lang3.reflect.FieldUtils {

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
                return null;
            }
            if (StringUtils.hasText(getter)) {
                char[] cs = getter.toCharArray();
                cs[0] += 32;
                return String.valueOf(cs);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
