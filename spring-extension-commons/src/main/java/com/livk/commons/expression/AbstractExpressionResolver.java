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
	protected final ContextFactory contextFactory = getContextFactory();

	@Override
	public final <T> T evaluate(String value, Map<String, ?> contextMap, Class<T> returnType) {
		return evaluate(value, new Context(contextMap), returnType);
	}

	@Override
	public final <T> T evaluate(String value, Method method, Object[] args, Class<T> returnType) {
		Map<String, Object> map = contextFactory.create(method, args);
		return evaluate(value, map, returnType);
	}

	@Override
	public final <T> T evaluate(String value, Method method, Object[] args, Map<String, ?> contextMap, Class<T> returnType) {
		Map<String, Object> map = contextFactory.merge(method, args, contextMap);
		return evaluate(value, map, returnType);
	}

	@Override
	public final String evaluate(String value, Context context) {
		return evaluate(value, context, String.class);
	}

	@Override
	public final String evaluate(String value, Map<String, ?> contextMap) {
		return evaluate(value, contextMap, String.class);
	}

	@Override
	public final String evaluate(String value, Method method, Object[] args) {
		return evaluate(value, method, args, String.class);
	}

	@Override
	public final String evaluate(String value, Method method, Object[] args, Map<String, ?> contextMap) {
		return evaluate(value, method, args, contextMap, String.class);
	}

	/**
	 * Gets context resolver.
	 *
	 * @return the context resolver
	 */
	protected ContextFactory getContextFactory() {
		return new DefaultContextFactory();
	}
}
