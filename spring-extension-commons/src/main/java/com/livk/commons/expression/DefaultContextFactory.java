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
import com.livk.commons.util.ObjectUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link Context}默认解析工厂
 * <p>
 * 使用spring DefaultParameterNameDiscoverer进行解析
 *
 * @author livk
 * @see DefaultParameterNameDiscoverer
 */
class DefaultContextFactory implements ContextFactory {

	private final Map<Method, String[]> parameterNamesCache = new ConcurrentHashMap<>(64);

	private final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

	@Override
	public Context create(Method method, Object[] args) {
		Assert.notNull(method, "method not be null");
		String[] parameterNames = this.parameterNamesCache.computeIfAbsent(method, discoverer::getParameterNames);
		if (ObjectUtils.isEmpty(parameterNames)) {
			return Context.create();
		}
		HashMap<String, Object> map = Maps.newHashMapWithExpectedSize(parameterNames.length);
		if (args != null && parameterNames.length == args.length) {
			for (int i = 0; i < parameterNames.length; i++) {
				map.put(parameterNames[i], args[i]);
			}
		}
		return Context.create(map);
	}

}
