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

package com.livk.autoconfigure.oss.support;

import com.livk.autoconfigure.oss.client.OSSClientFactory;
import com.livk.autoconfigure.oss.client.OSSClientFactoryLoader;
import com.livk.autoconfigure.oss.exception.OSSClientFactoryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Oss client factory pattern resolver.
 *
 * @author livk
 */
@Slf4j
class OSSClientFactoryPatternResolver implements OSSClientFactoryLoader {

	private final Map<String, OSSClientFactory<?>> factoryMap;

	/**
	 * Instantiates a new Oss client factory pattern resolver.
	 */
	public OSSClientFactoryPatternResolver(ApplicationContext applicationContext) {
		factoryMap = applicationContext.<OSSClientFactory<?>>getBeanProvider(ResolvableType.forClass(OSSClientFactory.class))
			.orderedStream()
			.collect(Collectors.toMap(OSSClientFactory::name, Function.identity()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> OSSClientFactory<T> loader(String prefix) {
		if (factoryMap.containsKey(prefix)) {
			return (OSSClientFactory<T>) factoryMap.get(prefix);
		}
		throw new OSSClientFactoryNotFoundException(prefix + " oss factory匹配失败,当前可用oss factory :" + factoryMap.keySet());
	}

}
