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

package com.livk.commons.expression.spring;

import com.livk.commons.SpringContextHolder;
import com.livk.commons.expression.Context;
import com.livk.commons.expression.ContextFactory;
import com.livk.commons.expression.ExpressionResolver;
import com.livk.commons.expression.ParseMethod;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class SpringExpressionResolverTest {

	private final static Object[] args = new Object[] { "livk" };

	private final static Map<String, String> map = Map.of("username", "livk");

	final ExpressionResolver resolver = new SpringExpressionResolver();

	final String springContextHolderName = SpringContextHolder.class.getName();

	private final Method method = ParseMethod.class.getDeclaredMethod("parseMethod", String.class);

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withPropertyValues("spring.application.root.name=livk")
		.withBean(SpringContextHolder.class, SpringContextHolder::new);

	SpringExpressionResolverTest() throws NoSuchMethodException {
	}

	@Test
	void evaluate() {
		Context context = ContextFactory.DEFAULT_FACTORY.create(method, args).putAll(Map.of("password", "123456"));
		assertTrue(resolver.evaluate("'livk'==#username", method, args, Boolean.class));
		assertEquals("livk", resolver.evaluate("#username", method, args));

		assertTrue(resolver.evaluate("'livk'==#username", map, Boolean.class));
		assertEquals("livk", resolver.evaluate("#username", map));

		assertEquals("root:livk", resolver.evaluate("root:#{#username}", method, args));
		assertEquals("root:livk:123456", resolver.evaluate("root:#{#username}:#{#password}", context));

		assertEquals("root:livk", resolver.evaluate("root:#{#username}", map));
		assertEquals("livk:" + System.getProperty("user.dir"),
				resolver.evaluate(("#{#username}:#{T(java.lang.System).getProperty(\"user.dir\")}"), map));

		assertEquals("root:livk", resolver.evaluate("root:#{#username}", method, args));
		assertEquals("livk", resolver.evaluate("#username", method, args));
		assertEquals("livk", resolver.evaluate("livk", method, args));

		assertEquals("root:livk:123456", resolver.evaluate("root:#{#username}:#{#password}", context));
		assertEquals("livk123456", resolver.evaluate("#username+#password", context));

		assertEquals("root:livk", resolver.evaluate("root:#{#username}", map));
		assertEquals("livk", resolver.evaluate("#username", map));
		assertEquals("livk:" + System.getProperty("user.dir"),
				resolver.evaluate("#{#username}:#{T(java.lang.System).getProperty(\"user.dir\")}", map));

		contextRunner.run(ctx -> {
			assertEquals("livk:livk", resolver.evaluate(
					"#{#username}:#{T(" + springContextHolderName + ").getProperty(\"spring.application.root.name\")}",
					map));

			assertEquals("livk:livk", resolver.evaluate(
					"#{#username}:#{T(" + springContextHolderName + ").getProperty(\"spring.application.root.name\")}",
					map));

			Map<String, Object> envMap = Map.of("username", "livk", "env",
					SpringContextHolder.getBean(Environment.class));
			assertEquals("livk:livk",
					resolver.evaluate("#{#username}:#{#env.getProperty(\"spring.application.root.name\")}", envMap));
		});
	}

}
