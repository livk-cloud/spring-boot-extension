/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.commons.function;

import com.livk.commons.bean.util.ClassUtils;
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
        String className = ClassUtils.convertResourcePathToClassName(serializedLambda.getImplClass());
        Class<?> type = ClassUtils.resolveClassName(className, ClassUtils.getDefaultClassLoader());
        return type.getDeclaredField(getName(function));
    }
}
