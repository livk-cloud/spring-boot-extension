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

import com.google.common.collect.Maps;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Expression evaluation context that holds variable bindings.
 * <p>
 * A sealed interface with a single permitted implementation, providing a fluent API for
 * building variable maps used during expression evaluation.
 * <p>
 * Usage example: <pre>{@code
 * Context context = Context.create()
 *     .put("username", "livk")
 *     .put("password", "123456");
 * }</pre>
 *
 * @author livk
 * @see ContextFactory
 * @see ExpressionResolver
 */
public sealed interface Context permits Context.ContextImpl {

	/**
	 * Create a new empty context.
	 * @return a new empty {@link Context} instance
	 */
	static Context create() {
		return new ContextImpl();
	}

	/**
	 * Create a new context initialized with the entries from the given map.
	 * @param map the initial variable bindings
	 * @return a new {@link Context} instance containing the map entries
	 */
	static Context create(Map<String, ?> map) {
		return new ContextImpl(map);
	}

	/**
	 * Add all entries from the given map to this context.
	 * @param m the map whose entries are to be added
	 * @return this context for method chaining
	 */
	Context putAll(Map<? extends String, ?> m);

	/**
	 * Add a single variable binding to this context.
	 * @param key the variable name
	 * @param value the variable value
	 * @return this context for method chaining
	 */
	Context put(String key, Object value);

	/**
	 * Return an unmodifiable view of this context as a {@link Map}.
	 * @return unmodifiable map of all variable bindings
	 */
	Map<String, Object> asMap();

	final class ContextImpl implements Context {

		private final Map<String, Object> variables;

		private ContextImpl() {
			variables = new HashMap<>();
		}

		private ContextImpl(Map<String, ?> map) {
			variables = Maps.newHashMap(map);
		}

		@Override
		public Context put(String key, Object value) {
			variables.put(key, value);
			return this;
		}

		@Override
		public Context putAll(@NonNull Map<? extends String, ?> m) {
			variables.putAll(m);
			return this;
		}

		@Override
		public Map<String, Object> asMap() {
			return Collections.unmodifiableMap(variables);
		}

	}

}
