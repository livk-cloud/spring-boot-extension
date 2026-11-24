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
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default {@link ContextFactory} implementation that uses Spring's
 * {@link DefaultParameterNameDiscoverer} to resolve method parameter names.
 * <p>
 * Parameter names are cached per method to avoid repeated reflection lookups. Indexed
 * aliases {@code p0}/{@code a0}, {@code p1}/{@code a1}, ... are always bound so
 * expressions remain usable when the compiler did not retain parameter names. Named
 * parameters never overwrite those indexed aliases.
 *
 * @author livk
 * @see DefaultParameterNameDiscoverer
 * @see ContextFactory
 */
class DefaultContextFactory implements ContextFactory {

	private final Map<Method, String[]> parameterNamesCache = new ConcurrentHashMap<>(64);

	private final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

	@Override
	public Context create(Method method, Object[] args) {
		Assert.notNull(method, "Method must not be null");
		if (args == null) {
			return Context.create();
		}
		String[] parameterNames = this.parameterNamesCache.computeIfAbsent(method, this.discoverer::getParameterNames);
		HashMap<String, Object> map = Maps.newHashMapWithExpectedSize(Math.max(args.length * 3, 4));
		for (int i = 0; i < args.length; i++) {
			Object value = args[i];
			map.put("a" + i, value);
			map.put("p" + i, value);
			if (parameterNames != null && i < parameterNames.length && parameterNames[i] != null
					&& !isIndexedAlias(parameterNames[i])) {
				map.put(parameterNames[i], value);
			}
		}
		return Context.create(map);
	}

	private static boolean isIndexedAlias(String name) {
		if (name.length() < 2) {
			return false;
		}
		char prefix = name.charAt(0);
		if (prefix != 'a' && prefix != 'p') {
			return false;
		}
		for (int i = 1; i < name.length(); i++) {
			if (!Character.isDigit(name.charAt(i))) {
				return false;
			}
		}
		return true;
	}

}
