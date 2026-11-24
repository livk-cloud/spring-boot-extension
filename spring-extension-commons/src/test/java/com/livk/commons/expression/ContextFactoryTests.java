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
		assertThat(context.asMap()).containsEntry("username", "livk")
			.containsEntry("p0", "livk")
			.containsEntry("a0", "livk")
			.hasSize(3);
	}

	@Test
	void createWithNullArgsReturnsEmptyContext() {
		Context context = contextFactory.create(method, null);
		assertThat(context.asMap()).isEmpty();
	}

	@Test
	void createWithExtraArgsStillBindsIndexedAliases() {
		Context context = contextFactory.create(method, new Object[] { "a", "b" });
		assertThat(context.asMap()).containsEntry("p0", "a")
			.containsEntry("a0", "a")
			.containsEntry("p1", "b")
			.containsEntry("a1", "b")
			.containsEntry("username", "a")
			.hasSize(5);
	}

	@Test
	void namedParameterDoesNotOverwriteIndexedAlias() throws NoSuchMethodException {
		Method colliding = IndexedAliasMethod.class.getDeclaredMethod("parse", String.class, String.class);
		Context context = contextFactory.create(colliding, new Object[] { "first", "second" });
		assertThat(context.asMap()).containsEntry("p0", "first")
			.containsEntry("a0", "first")
			.containsEntry("p1", "second")
			.containsEntry("a1", "second")
			.doesNotContainEntry("p0", "second");
	}

	@Test
	void shortOrNonIndexedNamesAreStillBound() throws NoSuchMethodException {
		Method method = AliasEdgeMethod.class.getDeclaredMethod("parse", String.class, String.class, String.class);
		Context context = contextFactory.create(method, new Object[] { "one", "two", "three" });
		assertThat(context.asMap()).containsEntry("p", "one")
			.containsEntry("px", "two")
			.containsEntry("orderId", "three")
			.containsEntry("p0", "one")
			.containsEntry("p1", "two")
			.containsEntry("p2", "three");
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

	@SuppressWarnings("unused")
	static final class IndexedAliasMethod {

		void parse(String first, String p0) {
		}

	}

	@SuppressWarnings("unused")
	static final class AliasEdgeMethod {

		void parse(String p, String px, String orderId) {
		}

	}

}
