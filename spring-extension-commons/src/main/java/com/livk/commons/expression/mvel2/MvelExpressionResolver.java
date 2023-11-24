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

package com.livk.commons.expression.mvel2;

import com.livk.commons.expression.Context;
import com.livk.commons.expression.ConverterExpressionResolver;
import org.mvel2.DataConversion;
import org.mvel2.MVELInterpretedRuntime;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.CachingMapVariableResolverFactory;

/**
 * 使用<a href="https://github.com/mvel/mvel">Mvel 2</a>实现的表达式解析器
 *
 * @author livk
 */
public class MvelExpressionResolver extends ConverterExpressionResolver<VariableResolverFactory, String> {

	@Override
	protected String compile(String value) {
		return value;
	}

	@Override
	protected VariableResolverFactory transform(Context context) {
		return new CachingMapVariableResolverFactory(context);
	}

	@Override
	protected <T> T calculate(String expression, VariableResolverFactory context, Class<T> returnType) {
		Object parse = new MVELInterpretedRuntime(expression, null, context).parse();
		return DataConversion.convert(parse, returnType);
	}

}
