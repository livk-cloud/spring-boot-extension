/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.commons.expression.mvel2;

import com.livk.commons.expression.CacheExpressionResolver;
import com.livk.commons.expression.Context;
import lombok.RequiredArgsConstructor;
import org.mvel2.DataConversion;
import org.mvel2.MVELInterpretedRuntime;
import org.mvel2.ParserContext;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.CachedMapVariableResolverFactory;
import org.mvel2.integration.impl.CachingMapVariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;

/**
 * 使用<a href="https://github.com/mvel/mvel">Mvel 2</a>实现的表达式解析器
 *
 * @author livk
 */
@RequiredArgsConstructor
public class MvelExpressionResolver extends CacheExpressionResolver<VariableResolverFactory, String> {

	private final ParserContext parserContext;

	/**
	 * 使用默认的ParserContext
	 *
	 * @see ParserContext
	 */
	public MvelExpressionResolver() {
		this(new ParserContext());
	}

	@Override
	protected String compile(String value) {
		return value;
	}

	@Override
	protected VariableResolverFactory transform(Context context) {
		VariableResolverFactory resolverFactory = new CachingMapVariableResolverFactory(context.asMap());
		VariableResolverFactory nextFactory = new CachedMapVariableResolverFactory(context.asMap(), resolverFactory);
		return new MapVariableResolverFactory(context.asMap(), nextFactory);
	}

	@Override
	protected <T> T calculate(String expression, VariableResolverFactory context, Class<T> returnType) {
		MVELInterpretedRuntime runtime = new MVELInterpretedRuntime(expression, null, context, parserContext);
		return DataConversion.convert(runtime.parse(), returnType);
	}

}
