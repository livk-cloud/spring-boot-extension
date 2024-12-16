/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.commons.expression;

import com.google.common.collect.Maps;
import org.springframework.lang.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析器上下文
 *
 * @author livk
 */
public interface Context {

	/**
	 * Instantiates a new Context.
	 */
	static Context create() {
		return new ContextImpl();
	}

	/**
	 * Instantiates a new Context.
	 * @param map the map
	 */
	static Context create(Map<String, ?> map) {
		return new ContextImpl(map);
	}

	/**
	 * Put all.
	 * @param m the m
	 */
	void putAll(Map<? extends String, ?> m);

	/**
	 * Put object.
	 * @param key the key
	 * @param value the value
	 * @return the object
	 */
	Object put(String key, Object value);

	Map<String, Object> asMap();

	class ContextImpl implements Context {

		private final Map<String, Object> variables;

		private ContextImpl() {
			variables = new HashMap<>();
		}

		private ContextImpl(Map<String, ?> map) {
			variables = Maps.newHashMap(map);
		}

		@Override
		public Object put(String key, Object value) {
			return variables.put(key, value);
		}

		@Override
		public void putAll(@NonNull Map<? extends String, ?> m) {
			variables.putAll(m);
		}

		@Override
		public Map<String, Object> asMap() {
			return Collections.unmodifiableMap(variables);
		}

	}

}
