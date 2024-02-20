/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.commons.expression.jexl3;

import com.livk.commons.expression.ExpressionResolver;
import com.livk.commons.expression.ParseMethodTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class JexlExpressionResolverTest {

	private final static Object[] args = new Object[] { "livk" };

	private final static Map<String, String> map = Map.of("username", "livk");

	final ExpressionResolver resolver = new JexlExpressionResolver();

	private final Method method = ParseMethodTest.class.getDeclaredMethod("parseMethod", String.class);

	JexlExpressionResolverTest() throws NoSuchMethodException {
	}

	@Test
	void evaluate() {
		assertTrue(resolver.evaluate("'livk'==username", method, args, Boolean.class));

		assertEquals("livk", resolver.evaluate("username", method, args));

		assertTrue(resolver.evaluate("'livk'==username", map, Boolean.class));
		assertEquals("livk", resolver.evaluate("username", map));

		assertEquals("root:livk", resolver.evaluate("'root:'+username", method, args));
		assertEquals("root:livk:123456",
				resolver.evaluate("'root:'+username+':'+password", method, args, Map.of("password", "123456")));

		assertEquals("root:livk", resolver.evaluate("'root:'+username", map));

		assertEquals("root:livk", resolver.evaluate("'root:'+username", method, args));
		assertEquals("livk", resolver.evaluate("username", method, args));
		assertEquals("livk", resolver.evaluate("'livk'", method, args));

		assertEquals("root:livk:123456",
				resolver.evaluate("'root:'+username+':'+password", method, args, Map.of("password", "123456")));
		assertEquals("livk123456", resolver.evaluate("username+password", method, args, Map.of("password", "123456")));

		assertEquals("root:livk", resolver.evaluate("'root:'+username", map));
		assertEquals("livk", resolver.evaluate("username", map));
	}

}
