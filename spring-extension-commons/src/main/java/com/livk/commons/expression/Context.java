/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.commons.expression;

import com.google.common.collect.Maps;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 解析器上下文
 *
 * @author livk
 */
public class Context implements Map<String, Object> {

	private final Map<String, Object> variables;

	/**
	 * Instantiates a new Context.
	 */
	public Context() {
		variables = new HashMap<>();
	}

	/**
	 * Instantiates a new Context.
	 * @param map the map
	 */
	public Context(Map<String, ?> map) {
		variables = Maps.newHashMap(map);
	}

	@Override
	public int size() {
		return variables.size();
	}

	@Override
	public boolean isEmpty() {
		return variables.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return variables.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return variables.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return variables.get(key);
	}

	@NonNull
	@Override
	public Object put(String key, Object value) {
		return put(key, value, false);
	}

	/**
	 * Put object.
	 * @param key the key
	 * @param value the value
	 * @param override the override
	 * @return the object
	 */
	public Object put(String key, Object value, boolean override) {
		return !override && variables.containsKey(key) ? variables.get(key) : variables.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return variables.remove(key);
	}

	@Override
	public void putAll(@NonNull Map<? extends String, ?> m) {
		variables.putAll(m);
	}

	/**
	 * Put all.
	 * @param m the m
	 * @param override the override
	 */
	public void putAll(Map<? extends String, ?> m, boolean override) {
		if (override) {
			variables.putAll(m);
		}
		else {
			for (Entry<? extends String, ?> entry : m.entrySet()) {
				if (!variables.containsKey(entry.getKey())) {
					variables.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}

	@Override
	public void clear() {
		variables.clear();
	}

	@NonNull
	@Override
	public Set<String> keySet() {
		return variables.keySet();
	}

	@NonNull
	@Override
	public Collection<Object> values() {
		return variables.values();
	}

	@NonNull
	@Override
	public Set<Entry<String, Object>> entrySet() {
		return variables.entrySet();
	}

}
