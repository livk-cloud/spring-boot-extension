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

package com.livk.commons.expression.spring;

import com.livk.commons.expression.Context;
import com.livk.commons.expression.ConverterExpressionResolver;
import com.livk.commons.expression.ExpressionResolver;
import org.springframework.context.expression.BeanExpressionContextAccessor;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.context.expression.EnvironmentAccessor;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeConverter;
import org.springframework.expression.spel.support.StandardTypeLocator;

/**
 * 使用Spring EL实现的表达式解析器
 *
 * @author livk
 * @see SpelExpressionParser
 */
public class SpringExpressionResolver extends ConverterExpressionResolver<EvaluationContext, Expression> implements ExpressionResolver {

	private final ExpressionParser expressionParser;

	private final ParserContext beanExpressionParserContext = new TemplateParserContext();

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

	@Override
	protected Expression compile(String value) {
		return this.expressionParser.parseExpression(value, this.beanExpressionParserContext);
	}

	@Override
	protected String wrapIfNecessary(String expression) {
		if (!expression.contains("#")) {
			return expression;
		}
		if (!expression.contains("#{")) {
			return "#{" + expression + "}";
		}
		return expression;
	}

	@Override
	protected EvaluationContext transform(Context context) {
		StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
		evaluationContext.addPropertyAccessor(new BeanExpressionContextAccessor());
		evaluationContext.addPropertyAccessor(new BeanFactoryAccessor());
		evaluationContext.addPropertyAccessor(new MapAccessor());
		evaluationContext.addPropertyAccessor(new EnvironmentAccessor());
		evaluationContext.setTypeLocator(new StandardTypeLocator());
		evaluationContext.setTypeConverter(new StandardTypeConverter());
		evaluationContext.setVariables(context);
		return evaluationContext;
	}

	@Override
	protected <T> T calculate(Expression expression, EvaluationContext context, Class<T> returnType) {
		return expression.getValue(context, returnType);
	}
}
