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
 * The interface Evaluation context factory.
 *
 * @author livk
 */
public interface EvaluationContextFactory {

    /**
     * The constant INSTANCE.
     */
    EvaluationContextFactory INSTANCE = new DefaultEvaluationContextFactory();

    /**
     * Eval evaluation context.
     *
     * @param method the method
     * @param args   the args
     * @return the evaluation context
     */
    default EvaluationContext eval(Method method, Object[] args) {
        return eval(method, args, Map.of());
    }

    /**
     * Eval evaluation context.
     *
     * @param expandMap the expand map
     * @return the evaluation context
     */
    default EvaluationContext eval(Map<String, ?> expandMap) {
        return eval(null, null, expandMap);
    }

    /**
     * Eval evaluation context.
     *
     * @param method    the method
     * @param args      the args
     * @param expandMap the expand map
     * @return the evaluation context
     */
    EvaluationContext eval(Method method, Object[] args, Map<String, ?> expandMap);
}
