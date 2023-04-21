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

package com.livk.commons.spring.spel;

import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * The interface Expression resolver.
 *
 * @author livk
 */
public interface ExpressionResolver {

    /**
     * Evaluate t.
     *
     * @param <T>        the type parameter
     * @param value      the value
     * @param context    the context
     * @param returnType the return type
     * @return the t
     */
    <T> T evaluate(String value, EvaluationContext context, Class<T> returnType);

    /**
     * Evaluate t.
     *
     * @param <T>        the type parameter
     * @param value      the value
     * @param method     the method
     * @param args       the args
     * @param returnType the return type
     * @return the t
     */
    default <T> T evaluate(String value, Method method, Object[] args, Class<T> returnType) {
        EvaluationContext evaluationContext = EvaluationContextFactory.INSTANCE.eval(method, args);
        return evaluate(value, evaluationContext, returnType);
    }

    /**
     * Evaluate string.
     *
     * @param value  the value
     * @param method the method
     * @param args   the args
     * @return the string
     */
    default String evaluate(String value, Method method, Object[] args) {
        return evaluate(value, method, args, String.class);
    }

    /**
     * Evaluate t.
     *
     * @param <T>        the type parameter
     * @param value      the value
     * @param expandMap  the expand map
     * @param returnType the return type
     * @return the t
     */
    default <T> T evaluate(String value, Map<String, ?> expandMap, Class<T> returnType) {
        EvaluationContext evaluationContext = EvaluationContextFactory.INSTANCE.eval(expandMap);
        return evaluate(value, evaluationContext, returnType);
    }

    /**
     * Evaluate string.
     *
     * @param value     the value
     * @param expandMap the expand map
     * @return the string
     */
    default String evaluate(String value, Map<String, ?> expandMap) {
        return evaluate(value, expandMap, String.class);
    }

    /**
     * Evaluate t.
     *
     * @param <T>        the type parameter
     * @param value      the value
     * @param method     the method
     * @param args       the args
     * @param expandMap  the expand map
     * @param returnType the return type
     * @return the t
     */
    default <T> T evaluate(String value, Method method, Object[] args, Map<String, ?> expandMap, Class<T> returnType) {
        EvaluationContext evaluationContext = EvaluationContextFactory.INSTANCE.eval(method, args, expandMap);
        return evaluate(value, evaluationContext, returnType);
    }

    /**
     * Evaluate string.
     *
     * @param value     the value
     * @param method    the method
     * @param args      the args
     * @param expandMap the expand map
     * @return the string
     */
    default String evaluate(String value, Method method, Object[] args, Map<String, ?> expandMap) {
        return evaluate(value, method, args, expandMap, String.class);
    }
}
