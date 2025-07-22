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

package com.livk.commons.expression.aviator;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.livk.commons.expression.Context;
import com.livk.commons.expression.ContextFactory;
import com.livk.commons.expression.ExpressionResolver;
import com.livk.commons.expression.ParseMethod;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class AviatorExpressionResolverTests {

	private final static Object[] args = new Object[] { "livk" };

	private final static Map<String, String> map = Map.of("username", "livk");

	final ExpressionResolver resolver = new AviatorExpressionResolver();

	private final Method method = ParseMethod.class.getDeclaredMethod("parseMethod", String.class);

	AviatorExpressionResolverTests() throws NoSuchMethodException {
	}

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withUserConfiguration(SpringConfig.class);

	@Test
	void evaluate() {
		Context context = ContextFactory.DEFAULT_FACTORY.create(method, args).putAll(Map.of("password", "123456"));
		assertTrue(resolver.evaluate("'livk'==#username", method, args, Boolean.class));

		assertEquals("livk", resolver.evaluate("#username", method, args));

		assertTrue(resolver.evaluate("'livk'==#username", map, Boolean.class));
		assertEquals("livk", resolver.evaluate("#username", map));

		assertEquals("root:livk", resolver.evaluate("'root:'+#username", method, args));
		assertEquals("root:livk:123456", resolver.evaluate("'root:'+#username+':'+#password", context));

		assertEquals("root:livk", resolver.evaluate("'root:'+#username", map));
		assertEquals("livk:" + System.getProperty("user.dir"),
				resolver.evaluate("#username+':'+System.getProperty(\"user.dir\")", map));

		assertEquals("root:livk", resolver.evaluate("'root:'+#username", method, args));
		assertEquals("livk", resolver.evaluate("#username", method, args));
		assertEquals("livk", resolver.evaluate("'livk'", method, args));

		assertEquals("root:livk:123456", resolver.evaluate("'root:'+#username+':'+#password", context));
		assertEquals("livk123456", resolver.evaluate("#username+#password", context));

		assertEquals("root:livk", resolver.evaluate("'root:'+#username", map));
		assertEquals("livk", resolver.evaluate("#username", map));
		assertEquals("livk:" + System.getProperty("user.dir"),
				resolver.evaluate("#username+':'+System.getProperty(\"user.dir\")", map));

		contextRunner.run(ctx -> {
			assertEquals("livk123", resolver.evaluate("add(x,y)", Map.of("x", "livk", "y", "123")));

			assertEquals(4,
					resolver.evaluate("ifElse(a==c,b,d)", Map.of("a", 1, "b", 2, "c", 3, "d", 4), Integer.class));
		});
	}

	@TestConfiguration
	static class SpringConfig {

		@Bean
		public AddFunction springStrAdd() {
			return new AddFunction();
		}

		@Bean
		public SpringContextFunctionLoader springContextFunctionLoader(ApplicationContext applicationContext) {
			return new SpringContextFunctionLoader(applicationContext);
		}

		@Bean
		public IfElseFunction ifElse() {
			return new IfElseFunction();
		}

	}

	static class IfElseFunction extends AbstractFunction {

		@Override
		public String getName() {
			return "ifElse";
		}

		@Override
		public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
			return FunctionUtils.getBooleanValue(arg1, env) ? arg2 : arg3;
		}

	}

	static class AddFunction extends AbstractFunction {

		@Override
		public String getName() {
			return "add";
		}

		@Override
		public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
			String left = FunctionUtils.getStringValue(arg1, env);
			String right = FunctionUtils.getStringValue(arg2, env);
			return FunctionUtils.wrapReturn(left + right);
		}

	}

}
