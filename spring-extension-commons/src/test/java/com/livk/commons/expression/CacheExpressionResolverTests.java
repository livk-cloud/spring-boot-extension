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

package com.livk.commons.expression;

import com.livk.commons.expression.ParseMethod;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.expression.ExpressionException;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class CacheExpressionResolverTests {

	final StubResolver resolver = new StubResolver();

	@Test
	void evaluateReturnsNullForEmptyExpression() {
		assertThat(resolver.resolve("").context(Map.of()).evaluate()).isNull();
		assertThat(resolver.resolve((String) null).context(Map.of()).evaluate()).isNull();
	}

	@Test
	void evaluateResolvesEnvironmentPlaceholders() {
		StandardEnvironment environment = new StandardEnvironment();
		environment.getPropertySources().addFirst(new MapPropertySource("test", Map.of("app.name", "livk")));
		resolver.setEnvironment(environment);

		assertThat(resolver.resolve("${app.name}").evaluate()).isEqualTo("livk");
	}

	@Test
	void evaluateCachesCompiledExpression() {
		resolver.resolve("cached").evaluate();
		resolver.resolve("cached").evaluate();

		assertThat(resolver.compileCount.get()).isEqualTo(1);
	}

	@Test
	void evaluateWrapsFailuresAsExpressionException() {
		assertThatThrownBy(() -> resolver.resolve("fail").evaluate()).isInstanceOf(ExpressionException.class)
			.hasMessageContaining("Expression parsing failed");
	}

	@Test
	void expressionSpecRejectsNullContextAndMethod() {
		assertThatThrownBy(() -> resolver.resolve("x").context((Context) null))
			.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> resolver.resolve("x").method(null)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void evaluateUsesEmptyContextWhenNoneProvided() {
		assertThat(resolver.resolve("literal").evaluate()).isEqualTo("literal");
	}

	@Test
	void evaluateWithMethodArgsUsesContextFactory() throws NoSuchMethodException {
		Method method = ParseMethod.class.getDeclaredMethod("parseMethod", String.class);
		assertThat(resolver.resolve("username").method(method, "livk").evaluate()).isEqualTo("livk");
	}

	static final class StubResolver extends CacheExpressionResolver<Map<String, Object>, String> {

		final AtomicInteger compileCount = new AtomicInteger();

		@Override
		protected String compile(String expression) {
			this.compileCount.incrementAndGet();
			return expression;
		}

		@Override
		protected Map<String, Object> transform(Context context) {
			return context.asMap();
		}

		@Override
		@SuppressWarnings("unchecked")
		protected <T> T calculate(String expression, Map<String, Object> context, Class<T> returnType) {
			if ("fail".equals(expression)) {
				throw new IllegalStateException("boom");
			}
			Object value = context.getOrDefault(expression, expression);
			return (T) returnType.cast(String.valueOf(value));
		}

	}

}
