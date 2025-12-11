/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.commons.expression;

import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 使用{@link ContextFactory}解析或者合并成{@link Context}
 *
 * @author livk
 * @see ContextFactory
 * @see Context
 */
public abstract class AbstractExpressionResolver implements ExpressionResolver {

	/**
	 * The Context resolver.
	 */
	protected final ContextFactory contextFactory;

	protected AbstractExpressionResolver(ContextFactory contextFactory) {
		Assert.notNull(contextFactory, "ContextFactory must not be null");
		this.contextFactory = contextFactory;
	}

	protected AbstractExpressionResolver() {
		this(new DefaultContextFactory());
	}

	@Override
	public final <T> T evaluate(String value, Map<String, ?> contextMap, Class<T> returnType) {
		Context context = Context.create(contextMap);
		return evaluate(value, context, returnType);
	}

	@Override
	public final <T> T evaluate(String value, Method method, Object[] args, Class<T> returnType) {
		Context context = contextFactory.create(method, args);
		return evaluate(value, context, returnType);
	}

	@Override
	public final String evaluate(String value, Context context) {
		return ExpressionResolver.super.evaluate(value, context);
	}

	@Override
	public final String evaluate(String value, Map<String, ?> contextMap) {
		return ExpressionResolver.super.evaluate(value, contextMap);
	}

	@Override
	public final String evaluate(String value, Method method, Object[] args) {
		return ExpressionResolver.super.evaluate(value, method, args);
	}

}
