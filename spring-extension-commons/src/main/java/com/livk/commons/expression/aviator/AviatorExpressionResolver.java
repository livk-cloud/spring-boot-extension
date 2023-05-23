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

package com.livk.commons.expression.aviator;

import com.google.common.collect.Maps;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.livk.commons.expression.CacheExpressionResolver;
import com.livk.commons.expression.ExpressionResolver;
import org.springframework.expression.EvaluationContext;

import java.util.HashMap;
import java.util.List;

/**
 * 使用<a href="https://github.com/killme2008/aviator">Aviator</a>实现的表达式解析器
 *
 * @author livk
 * @see AviatorEvaluator
 */
public class AviatorExpressionResolver extends CacheExpressionResolver<Expression> implements ExpressionResolver {

    @Override
    protected Expression compile(String value) {
        return AviatorEvaluator.compile(value);
    }

    @Override
    protected <T> T calculate(Expression expression, EvaluationContext context, Class<T> returnType) {
        return returnType.cast(this.execute(expression, context));
    }

    private Object execute(Expression expression, EvaluationContext context) {
        List<String> variableNames = expression.getVariableNames();
        HashMap<String, Object> env = Maps.newHashMapWithExpectedSize(variableNames.size());
        for (String name : variableNames) {
            Object lookup = context.lookupVariable(name);
            if (lookup != null) {
                env.put(name, lookup);
            }
        }
        return expression.execute(env);
    }
}
