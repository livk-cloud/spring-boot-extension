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

import lombok.Setter;
import org.springframework.core.env.Environment;
import org.springframework.expression.*;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Spring expression resolver.
 *
 * @author livk
 */
public class SpringExpressionResolver implements ExpressionResolver {

    private final ExpressionParser expressionParser;

    private final Map<String, Expression> expressionCache = new ConcurrentHashMap<>(256);

    private final ParserContext beanExpressionParserContext = new TemplateParserContext();

    @Setter
    private Environment environment;

    /**
     * Instantiates a new Spring expression resolver.
     */
    public SpringExpressionResolver() {
        this.expressionParser = new SpelExpressionParser();
    }

    /**
     * Instantiates a new Spring expression resolver.
     *
     * @param beanClassLoader the bean class loader
     */
    public SpringExpressionResolver(ClassLoader beanClassLoader) {
        this.expressionParser = new SpelExpressionParser(new SpelParserConfiguration(null, beanClassLoader));
    }

    /**
     * Instantiates a new Spring expression resolver.
     *
     * @param environment the environment
     */
    public SpringExpressionResolver(Environment environment) {
        this();
        this.environment = environment;
    }

    @Override
    public <T> T evaluate(String value, EvaluationContext context, Class<T> returnType) {
        if (!StringUtils.hasLength(value)) {
            return null;
        }
        try {
            value = wrapIfNecessary(value);
            if (environment != null) {
                value = environment.resolvePlaceholders(value);
            }
            Expression expression = this.expressionCache.get(value);
            if (expression == null) {
                expression = this.expressionParser.parseExpression(value, this.beanExpressionParserContext);
                this.expressionCache.put(value, expression);
            }
            return expression.getValue(context, returnType);
        } catch (Throwable ex) {
            throw new ExpressionException("Expression parsing failed", ex);
        }
    }

    private String wrapIfNecessary(String expression) {
        if (!expression.contains("#")) {
            return expression;
        }
        if (!expression.contains("#{")) {
            return "#{" + expression + "}";
        }
        return expression;
    }
}
