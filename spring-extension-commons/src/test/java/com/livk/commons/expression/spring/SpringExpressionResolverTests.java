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
	void evaluate() {
		Context context = ContextFactory.DEFAULT_FACTORY.create(method, args).putAll(Map.of("password", "123456"));
		assertThat(resolver.evaluate("'livk'==#username", method, args, Boolean.class)).isTrue();
		assertThat(resolver.evaluate("#username", method, args)).isEqualTo("livk");

		assertThat(resolver.evaluate("'livk'==#username", map, Boolean.class)).isTrue();
		assertThat(resolver.evaluate("#username", map)).isEqualTo("livk");

		assertThat(resolver.evaluate("root:#{#username}", method, args)).isEqualTo("root:livk");
		assertThat(resolver.evaluate("root:#{#username}:#{#password}", context)).isEqualTo("root:livk:123456");

		assertThat(resolver.evaluate("root:#{#username}", map)).isEqualTo("root:livk");
		assertThat(resolver.evaluate("#{#username}:#{T(java.lang.System).getProperty(\"user.dir\")}", map))
			.isEqualTo("livk:" + System.getProperty("user.dir"));

		assertThat(resolver.evaluate("root:#{#username}", method, args)).isEqualTo("root:livk");
		assertThat(resolver.evaluate("#username", method, args)).isEqualTo("livk");
		assertThat(resolver.evaluate("livk", method, args)).isEqualTo("livk");

		assertThat(resolver.evaluate("root:#{#username}:#{#password}", context)).isEqualTo("root:livk:123456");
		assertThat(resolver.evaluate("#username+#password", context)).isEqualTo("livk123456");

		assertThat(resolver.evaluate("root:#{#username}", map)).isEqualTo("root:livk");
		assertThat(resolver.evaluate("#username", map)).isEqualTo("livk");
		assertThat(resolver.evaluate("#{#username}:#{T(java.lang.System).getProperty(\"user.dir\")}", map))
			.isEqualTo("livk:" + System.getProperty("user.dir"));

		contextRunner.run(ctx -> {
			assertThat(resolver.evaluate(
					"#{#username}:#{T(" + springContextHolderName + ").getProperty(\"spring.application.root.name\")}",
					map))
				.isEqualTo("livk:livk");

			assertThat(resolver.evaluate(
					"#{#username}:#{T(" + springContextHolderName + ").getProperty(\"spring.application.root.name\")}",
					map))
				.isEqualTo("livk:livk");

			Map<String, Object> envMap = Map.of("username", "livk", "env",
					SpringContextHolder.getBean(Environment.class));
			assertThat(resolver.evaluate("#{#username}:#{#env.getProperty(\"spring.application.root.name\")}", envMap))
				.isEqualTo("livk:livk");
		});

	}

}
