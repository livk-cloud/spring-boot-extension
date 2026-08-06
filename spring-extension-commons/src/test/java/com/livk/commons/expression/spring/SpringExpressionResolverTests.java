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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class SpringExpressionResolverTests {

	private final Object[] args = new Object[] { "livk" };

	private final Map<String, String> map = Map.of("username", "livk");

	final ExpressionResolver resolver = new SpringExpressionResolver();

	final String springContextHolderName = SpringContextHolder.class.getName();

	private final Method method = ParseMethod.class.getDeclaredMethod("parseMethod", String.class);

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withPropertyValues("spring.application.root.name=livk")
		.withBean(SpringContextHolder.class, SpringContextHolder::new);

	SpringExpressionResolverTests() throws NoSuchMethodException {
	}

	@Test
	void evaluateWithMethodArgs() {
		assertThat(resolver.resolve("'livk'==#username").method(method, args).evaluate(Boolean.class)).isTrue();
		assertThat(resolver.resolve("#username").method(method, args).evaluate()).isEqualTo("livk");
	}

	@Test
	void evaluateWithMap() {
		assertThat(resolver.resolve("'livk'==#username").context(map).evaluate(Boolean.class)).isTrue();
		assertThat(resolver.resolve("#username").context(map).evaluate()).isEqualTo("livk");
	}

	@Test
	void evaluateTemplateExpressionWithMethodArgs() {
		assertThat(resolver.resolve("root:#{#username}").method(method, args).evaluate()).isEqualTo("root:livk");
	}

	@Test
	void evaluateTemplateExpressionWithContext() {
		Context context = ContextFactory.DEFAULT_FACTORY.create(method, args).putAll(Map.of("password", "123456"));
		assertThat(resolver.resolve("root:#{#username}:#{#password}").context(context).evaluate())
			.isEqualTo("root:livk:123456");
	}

	@Test
	void evaluateTemplateExpressionWithMap() {
		assertThat(resolver.resolve("root:#{#username}").context(map).evaluate()).isEqualTo("root:livk");
	}

	@Test
	void evaluateWithTypeInvocation() {
		assertThat(resolver.resolve("#{#username}:#{T(java.lang.System).getProperty(\"user.dir\")}")
			.context(map)
			.evaluate()).isEqualTo("livk:" + System.getProperty("user.dir"));
	}

	@Test
	void evaluatePlainStringPassesThrough() {
		assertThat(resolver.resolve("livk").method(method, args).evaluate()).isEqualTo("livk");
	}

	@Test
	void evaluateConcatenationWithContext() {
		Context context = ContextFactory.DEFAULT_FACTORY.create(method, args).putAll(Map.of("password", "123456"));
		assertThat(resolver.resolve("#username+#password").context(context).evaluate()).isEqualTo("livk123456");
	}

	@Test
	void evaluateWithSpringContextHolder() {
		contextRunner.run(ctx -> {
			assertThat(resolver
				.resolve("#{#username}:#{T(" + springContextHolderName
						+ ").getProperty(\"spring.application.root.name\")}")
				.context(map)
				.evaluate()).isEqualTo("livk:livk");
		});
	}

	@Test
	void evaluateWithEnvironmentBean() {
		contextRunner.run(ctx -> {
			Map<String, Object> envMap = Map.of("username", "livk", "env",
					SpringContextHolder.getBean(Environment.class));
			assertThat(resolver.resolve("#{#username}:#{#env.getProperty(\"spring.application.root.name\")}")
				.context(envMap)
				.evaluate()).isEqualTo("livk:livk");
		});
	}

	@Test
	void evaluateWithEmptyContext() {
		assertThat(resolver.resolve("livk").evaluate()).isEqualTo("livk");
	}

	@Test
	void evaluateWithCustomContextFactory() {
		String result = resolver.resolve("#username")
			.contextFactory(ContextFactory.DEFAULT_FACTORY)
			.method(method, args)
			.evaluate();
		assertThat(result).isEqualTo("livk");
	}

}
