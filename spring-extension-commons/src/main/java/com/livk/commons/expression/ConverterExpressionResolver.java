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

import org.springframework.util.Assert;

/**
 * The type Converter expression resolver.
 *
 * @param <C> the type parameter
 * @param <E> the type parameter
 * @author livk
 * @see com.livk.commons.expression.jexl3.JexlExpressionResolver
 * @see com.livk.commons.expression.mvel2.MvelExpressionResolver
 * @see com.livk.commons.expression.spring.SpringExpressionResolver
 */
public abstract class ConverterExpressionResolver<C, E> extends CacheExpressionResolver<E> {

	@Override
	protected final <T> T calculate(E expression, Context context, Class<T> returnType) {
		C frameworkContext = transform(context);
		Assert.notNull(frameworkContext, "frameworkContext not be null");
		return calculate(expression, frameworkContext, returnType);
	}

	/**
	 * Transform c.
	 *
	 * @param context the context
	 * @return the c
	 */
	protected abstract C transform(Context context);

	/**
	 * Calculate t.
	 *
	 * @param <T>        the type parameter
	 * @param expression the expression
	 * @param context    the context
	 * @param returnType the return type
	 * @return the t
	 */
	protected abstract <T> T calculate(E expression, C context, Class<T> returnType);
}
