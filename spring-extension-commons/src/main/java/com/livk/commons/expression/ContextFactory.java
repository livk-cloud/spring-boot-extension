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

import java.lang.reflect.Method;
import java.util.Map;

/**
 * The interface Context resolver.
 *
 * @author livk
 */
@FunctionalInterface
public interface ContextFactory {

	/**
	 * Create context.
	 *
	 * @param method the method
	 * @param args   the args
	 * @return the context
	 */
	Context create(Method method, Object[] args);

	/**
	 * Merge map.
	 *
	 * @param method    the method
	 * @param args      the args
	 * @param expandMap the expand map
	 * @return the map
	 */
	default Context merge(Method method, Object[] args, Map<String, ?> expandMap) {
		Context context = create(method, args);
		context.putAll(expandMap, false);
		return context;
	}
}
