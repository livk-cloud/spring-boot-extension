/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.commons.expression.aviator;

import com.googlecode.aviator.FunctionLoader;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * ServiceFunctionLoader
 * </p>
 *
 * @author livk
 */
public class ServiceFunctionLoader implements FunctionLoader {

	private final Map<String, AviatorFunction> functions = new ConcurrentHashMap<>();

	public ServiceFunctionLoader() {
		load();
	}

	@Override
	public AviatorFunction onFunctionNotFound(String name) {
		return functions.get(name);
	}

	public void load() {
		for (AviatorFunction function : ServiceLoader.load(AviatorFunction.class)) {
			this.functions.put(function.getName(), function);
		}
	}

}
