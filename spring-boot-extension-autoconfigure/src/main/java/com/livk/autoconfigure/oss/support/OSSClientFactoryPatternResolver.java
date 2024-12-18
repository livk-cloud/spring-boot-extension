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

package com.livk.autoconfigure.oss.support;

import com.livk.autoconfigure.oss.client.OSSClientFactory;
import com.livk.autoconfigure.oss.client.OSSClientFactoryLoader;
import com.livk.autoconfigure.oss.exception.OSSClientFactoryNotFoundException;
import com.livk.autoconfigure.oss.support.minio.MinioClientFactory;
import com.livk.commons.util.ClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type Oss client factory pattern resolver.
 *
 * @author livk
 */
@Slf4j
class OSSClientFactoryPatternResolver implements OSSClientFactoryLoader {

	private static final Set<OSSClientFactory<?>> FACTORIES = new HashSet<>();

	static {
		register();
	}

	private static void register() {
		if (ClassUtils.isPresent("io.minio.MinioClient")) {
			FACTORIES.add(new MinioClientFactory());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> OSSClientFactory<T> loader(String prefix) {
		for (OSSClientFactory<?> factory : FACTORIES) {
			if (factory.name().equals(prefix)) {
				return (OSSClientFactory<T>) factory;
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("oss factory匹配失败, 加载Spring SPI");
		}
		List<OSSClientFactoryLoader> factoryLoaders = SpringFactoriesLoader.loadFactories(OSSClientFactoryLoader.class,
				ClassUtils.getDefaultClassLoader());
		for (OSSClientFactoryLoader factoryLoader : factoryLoaders) {
			OSSClientFactory<T> clientFactory = factoryLoader.loader(prefix);
			if (clientFactory != null) {
				return clientFactory;
			}
		}
		throw new OSSClientFactoryNotFoundException(prefix + " oss factory匹配失败");
	}

}
