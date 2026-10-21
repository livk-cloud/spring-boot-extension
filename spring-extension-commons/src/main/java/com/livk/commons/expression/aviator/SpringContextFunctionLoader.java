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

package com.livk.commons.expression.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.FunctionLoader;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring-based FunctionLoader for Aviator.
 * <p>
 * 从IOC容器中获取AviatorFunction.
 *
 * @author livk
 * @see AviatorFunction
 */
@RequiredArgsConstructor
public class SpringContextFunctionLoader implements FunctionLoader, InitializingBean, DisposableBean {

	private final ApplicationContext applicationContext;

	private volatile Map<String, AviatorFunction> functionMap;

	@Override
	public AviatorFunction onFunctionNotFound(String name) {
		return getFunctionMap().get(name);
	}

	private Map<String, AviatorFunction> getFunctionMap() {
		Map<String, AviatorFunction> result = this.functionMap;
		if (result == null) {
			synchronized (this) {
				result = this.functionMap;
				if (result == null) {
					Map<String, AviatorFunction> beans = this.applicationContext.getBeansOfType(AviatorFunction.class);
					Map<String, AviatorFunction> map = new HashMap<>(beans);
					beans.values().forEach((fn) -> map.putIfAbsent(fn.getName(), fn));
					result = Map.copyOf(map);
					this.functionMap = result;
				}
			}
		}
		return result;
	}

	@Override
	public void afterPropertiesSet() {
		AviatorEvaluator.addFunctionLoader(this);
	}

	@Override
	public void destroy() {
		AviatorEvaluator.removeFunctionLoader(this);
	}

}
