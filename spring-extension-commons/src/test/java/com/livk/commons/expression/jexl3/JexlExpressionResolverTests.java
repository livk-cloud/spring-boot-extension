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

package com.livk.commons.expression.jexl3;

import com.livk.commons.expression.Context;
import com.livk.commons.expression.ContextFactory;
import com.livk.commons.expression.ExpressionResolver;
import com.livk.commons.expression.ParseMethod;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class JexlExpressionResolverTests {

	private final Object[] args = new Object[] { "livk" };

	private final Map<String, String> map = Map.of("username", "livk");

	final ExpressionResolver resolver = new JexlExpressionResolver();

	private final Method method = ParseMethod.class.getDeclaredMethod("parseMethod", String.class);

	JexlExpressionResolverTests() throws NoSuchMethodException {
	}

	@Test
	void evaluate() {
		Context context = ContextFactory.DEFAULT_FACTORY.create(method, args).putAll(Map.of("password", "123456"));
		assertThat(resolver.evaluate("'livk'==username", method, args, Boolean.class)).isTrue();

		assertThat(resolver.evaluate("username", method, args)).isEqualTo("livk");

		assertThat(resolver.evaluate("'livk'==username", map, Boolean.class)).isTrue();

		assertThat(resolver.evaluate("username", map)).isEqualTo("livk");

		assertThat(resolver.evaluate("'root:'+username", method, args)).isEqualTo("root:livk");

		assertThat(resolver.evaluate("'root:'+username+':'+password", context)).isEqualTo("root:livk:123456");

		assertThat(resolver.evaluate("'root:'+username", map)).isEqualTo("root:livk");

		assertThat(resolver.evaluate("'root:'+username", method, args)).isEqualTo("root:livk");

		assertThat(resolver.evaluate("username", method, args)).isEqualTo("livk");

		assertThat(resolver.evaluate("'livk'", method, args)).isEqualTo("livk");

		assertThat(resolver.evaluate("'root:'+username+':'+password", context)).isEqualTo("root:livk:123456");

		assertThat(resolver.evaluate("username+password", context)).isEqualTo("livk123456");

		assertThat(resolver.evaluate("'root:'+username", map)).isEqualTo("root:livk");

		assertThat(resolver.evaluate("username", map)).isEqualTo("livk");

	}

}
