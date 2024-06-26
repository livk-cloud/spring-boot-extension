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

package com.livk.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.GenericTypeResolver;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * AbstractServiceLoad
 * </p>
 *
 * @author livk
 */
@Slf4j
public abstract class AbstractServiceLoad<T> implements InitializingBean {

	protected Map<String, T> servicesMap;

	@SuppressWarnings("unchecked")
	private Class<T> getServiceClass() {
		return (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), AbstractServiceLoad.class);
	}

	@Override
	public void afterPropertiesSet() {
		servicesMap = ServiceLoader.load(getServiceClass())
			.stream()
			.map(ServiceLoader.Provider::get)
			.collect(Collectors.toMap(this::getKey, Function.identity()));
		log.info("data:{}", servicesMap);
	}

	protected abstract String getKey(T t);

}
