/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
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
 * 上下键创建工厂
 *
 * @author livk
 * @see DefaultContextFactory
 */
@FunctionalInterface
public interface ContextFactory {

	ContextFactory DEFAULT_FACTORY = new DefaultContextFactory();

	/**
	 * 根据方法与参数创建上下文
	 * @param method the method
	 * @param args the args
	 * @return the context
	 */
	Context create(Method method, Object[] args);

	/**
	 * 合并上下文
	 * @param method the method
	 * @param args the args
	 * @param expandMap the expand map
	 * @return the map
	 * @deprecated use {@link Context#putAll(Map)}
	 */
	@Deprecated(since = "1.4.2", forRemoval = true)
	default Context merge(Method method, Object[] args, Map<String, ?> expandMap) {
		Context context = create(method, args);
		return context.putAll(expandMap);
	}

}
