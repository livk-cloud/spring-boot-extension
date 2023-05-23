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
 * EvaluationContext构建工厂
 *
 * @author livk
 * @see EvaluationContext
 * @see org.springframework.expression.spel.support.StandardEvaluationContext
 */
public interface EvaluationContextFactory {

    /**
     * 提供默认实例
     */
    EvaluationContextFactory INSTANCE = new DefaultEvaluationContextFactory();

    /**
     * 将Method与对应参数解析成EvaluationContext
     *
     * @param method method
     * @param args   args
     * @return EvaluationContext
     */
    default EvaluationContext eval(Method method, Object[] args) {
        return eval(method, args, Map.of());
    }

    /**
     * 将Map解析成EvaluationContext
     *
     * @param expandMap 环境参数
     * @return EvaluationContext
     */
    default EvaluationContext eval(Map<String, ?> expandMap) {
        return eval(null, null, expandMap);
    }

    /**
     * 将Method与对应参数、Map解析成EvaluationContext
     *
     * @param method    method
     * @param args      args
     * @param expandMap 环境参数
     * @return EvaluationContext
     */
    EvaluationContext eval(Method method, Object[] args, Map<String, ?> expandMap);
}
