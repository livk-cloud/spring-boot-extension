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

package com.livk.commons.expression;

import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 表达式通用解析器
 *
 * @author livk
 * @see EvaluationContext
 * @see EvaluationContextFactory
 */
public interface ExpressionResolver {

    /**
     * 将表达式解析，并转成相对应的类型
     *
     * @param <T>        泛型
     * @param value      表达式
     * @param context    解析上下文
     * @param returnType 返回类型
     * @return T
     */
    <T> T evaluate(String value, EvaluationContext context, Class<T> returnType);

    /**
     * 根据Method将表达式解析，并转成相对应的类型
     *
     * @param <T>        泛型
     * @param value      表达式
     * @param method     method
     * @param args       args
     * @param returnType 返回类型
     * @return T
     */
    default <T> T evaluate(String value, Method method, Object[] args, Class<T> returnType) {
        EvaluationContext evaluationContext = contextFactory().eval(method, args);
        return evaluate(value, evaluationContext, returnType);
    }

    /**
     * 根据Method将表达式解析，并转成String
     *
     * @param value  表达式
     * @param method method
     * @param args   args
     * @return string
     */
    default String evaluate(String value, Method method, Object[] args) {
        return evaluate(value, method, args, String.class);
    }

    /**
     * 根据Map环境信息将表达式解析，并转成相应的类型
     *
     * @param <T>        泛型
     * @param value      表达式
     * @param expandMap  解析上下文环境数据
     * @param returnType 返回类型
     * @return T
     */
    default <T> T evaluate(String value, Map<String, ?> expandMap, Class<T> returnType) {
        EvaluationContext evaluationContext = contextFactory().eval(expandMap);
        return evaluate(value, evaluationContext, returnType);
    }

    /**
     * 根据Map环境信息将表达式解析，并转成String
     *
     * @param value     表达式
     * @param expandMap 解析上下文环境数据
     * @return string
     */
    default String evaluate(String value, Map<String, ?> expandMap) {
        return evaluate(value, expandMap, String.class);
    }

    /**
     * 根据Method和Map环境信息将表达式解析，并转成相对应的类型
     *
     * @param <T>        泛型
     * @param value      表达式
     * @param method     method
     * @param args       args
     * @param expandMap  解析上下文环境数据
     * @param returnType 返回类型
     * @return T
     */
    default <T> T evaluate(String value, Method method, Object[] args, Map<String, ?> expandMap, Class<T> returnType) {
        EvaluationContext evaluationContext = contextFactory().eval(method, args, expandMap);
        return evaluate(value, evaluationContext, returnType);
    }

    /**
     * 根据Method和Map环境信息将表达式解析，并转成String
     *
     * @param value     表达式
     * @param method    method
     * @param args      args
     * @param expandMap 解析上下文环境数据
     * @return string
     */
    default String evaluate(String value, Method method, Object[] args, Map<String, ?> expandMap) {
        return evaluate(value, method, args, expandMap, String.class);
    }

    /**
     * 提供相应的EvaluationContextFactory
     * <p>
     * 重写此方法提供解析工厂的替换
     *
     * @return EvaluationContextFactory
     */
    default EvaluationContextFactory contextFactory() {
        return EvaluationContextFactory.INSTANCE;
    }
}
