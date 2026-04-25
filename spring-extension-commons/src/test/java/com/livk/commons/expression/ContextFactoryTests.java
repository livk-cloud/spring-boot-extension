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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class ContextFactoryTests {

	private final Method method = ParseMethod.class.getDeclaredMethod("parseMethod", String.class);

	final ContextFactory contextFactory = new DefaultContextFactory();

	ContextFactoryTests() throws NoSuchMethodException {
	}

	@Test
	void createFromMethodAndArgs() {
		Context context = contextFactory.create(method, new String[] { "livk" });
		assertThat(context.asMap()).containsExactlyInAnyOrderEntriesOf(Map.of("username", "livk"));
	}

	@Test
	void createWithNullArgsReturnsEmptyContext() {
		Context context = contextFactory.create(method, null);
		assertThat(context.asMap()).isEmpty();
	}

	@Test
	void createWithMismatchedArgsLengthReturnsEmptyContext() {
		Context context = contextFactory.create(method, new Object[] { "a", "b" });
		assertThat(context.asMap()).isEmpty();
	}

	@Test
	void createEmptyContext() {
		Context context = Context.create();
		assertThat(context.asMap()).isEmpty();
	}

	@Test
	void createFromMap() {
		Context context = Context.create(Map.of("key", "value"));
		assertThat(context.asMap()).containsExactlyInAnyOrderEntriesOf(Map.of("key", "value"));
	}

	@Test
	void putAddsEntry() {
		Context context = Context.create().put("username", "livk").put("password", "123456");
		assertThat(context.asMap()).containsEntry("username", "livk").containsEntry("password", "123456").hasSize(2);
	}

	@Test
	void putAllMergesEntries() {
		Context context = Context.create().put("username", "root").putAll(Map.of("username", "livk", "extra", "val"));
		assertThat(context.asMap()).containsEntry("username", "livk").containsEntry("extra", "val").hasSize(2);
	}

	@Test
	void asMapReturnsUnmodifiableMap() {
		Context context = Context.create(Map.of("key", "value"));
		assertThatThrownBy(() -> context.asMap().put("new", "entry")).isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	void defaultFactoryConstantIsNotNull() {
		assertThat(ContextFactory.DEFAULT_FACTORY).isNotNull();
		Context context = ContextFactory.DEFAULT_FACTORY.create(method, new String[] { "livk" });
		assertThat(context.asMap()).containsEntry("username", "livk");
	}

}
