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

package com.livk.autoconfigure.oss.support;

import com.livk.autoconfigure.oss.OSSProperties;
import com.livk.autoconfigure.oss.factory.OSSClientFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.ResolvableType;

import java.util.Optional;

/**
 * The type Abstract service.
 *
 * @param <T> the type parameter
 */
public abstract non-sealed class AbstractService<T> implements OSSOperations, ApplicationContextAware {

	/**
	 * The Client.
	 */
	protected T client;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		OSSProperties properties = applicationContext.getBean(OSSProperties.class);

		ResolvableType type = ResolvableType.forClass(OSSClientFactory.class);
		Optional<OSSClientFactory<T>> optional = applicationContext.<OSSClientFactory<T>>getBeanProvider(type)
			.orderedStream()
			.filter(factory -> factory.name().equals(properties.getType()))
			.findFirst();

		if (optional.isPresent()) {
			OSSClientFactory<T> factory = optional.get();
			this.client = factory.instance(properties.getEndpoint(), properties.getAccessKey(),
					properties.getSecretKey(), properties.getRegion());

			if (applicationContext instanceof GenericApplicationContext context) {
				ResolvableType resolvableType = ResolvableType.forInstance(client);
				BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(resolvableType,
						() -> client);
				AbstractBeanDefinition beanDefinition = definitionBuilder.getBeanDefinition();
				context.registerBeanDefinition(factory.name(), beanDefinition);
			}
		}
	}

}
