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

import com.livk.commons.util.ObjectUtils;
import lombok.Setter;
import org.springframework.core.env.Environment;
import org.springframework.expression.ExpressionException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用缓存机制，减少重新构建EXPRESSION表达式的次数
 *
 * @param <EXPRESSION> 表达式泛型
 * @author livk
 * @see com.livk.commons.expression.aviator.AviatorExpressionResolver
 * @see Environment
 */
public abstract class CacheExpressionResolver<EXPRESSION> extends AbstractExpressionResolver  {

	private final Map<String, EXPRESSION> expressionCache = new ConcurrentHashMap<>(256);

	@Setter
	private Environment environment;

	@Override
	public <T> T evaluate(String value, Context context, Class<T> returnType) {
		if (ObjectUtils.isEmpty(value)) {
			return null;
		}
		try {
			value = wrapIfNecessary(value);
			if (environment != null) {
				value = environment.resolvePlaceholders(value);
			}
			EXPRESSION expression = this.expressionCache.get(value);
			if (expression == null) {
				expression = compile(value);
				this.expressionCache.put(value, expression);
			}
			return calculate(expression, context, returnType);
		} catch (Throwable ex) {
			throw new ExpressionException("Expression parsing failed", ex);
		}
	}

	/**
	 * 对表达式进行数据包装，如果有必要
	 *
	 * @param expression 表达式
	 * @return 包装后的表达式 string
	 */
	protected String wrapIfNecessary(String expression) {
		return expression;
	}

	/**
	 * 对表达式进行处理生成EXPRESSION
	 *
	 * @param value 表达式
	 * @return EXPRESSION expression
	 * @throws Throwable the throwable
	 */
	protected abstract EXPRESSION compile(String value) throws Throwable;

	/**
	 * 表达式计算
	 *
	 * @param <T>        泛型
	 * @param expression 表达式
	 * @param context    环境上下文
	 * @param returnType 返回类型
	 * @return T
	 * @throws Throwable the throwable
	 */
	protected abstract <T> T calculate(EXPRESSION expression, Context context, Class<T> returnType) throws Throwable;
}
