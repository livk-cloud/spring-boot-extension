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

import com.livk.autoconfigure.oss.OSSProperties;
import com.livk.autoconfigure.oss.client.OSSClientFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.ResolvableType;

/**
 * The type Abstract service.
 *
 * @param <T> the type parameter
 */
public abstract non-sealed class AbstractService<T> implements OSSOperations, ApplicationContextAware {

    /**
     * The Resolver.
     */
    protected final OSSClientFactoryPatternResolver resolver = new OSSClientFactoryPatternResolver();

    /**
     * The Client.
     */
    protected T client;

    /**
     * The Name.
     */
    protected final String name;

    /**
     * Instantiates a new Abstract service.
     *
     * @param properties the properties
     */
    protected AbstractService(OSSProperties properties) {
        OSSClientFactory<T> factory = resolver.loader(properties.getPrefix());
        this.name = factory.name();
        this.client = factory.instance(properties.getEndpoint(), properties.getAccessKey(), properties.getSecretKey());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ResolvableType resolvableType = ResolvableType.forInstance(client);
        BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(resolvableType, () -> client);
        if (applicationContext instanceof GenericApplicationContext context) {
            context.registerBeanDefinition(name, definitionBuilder.getBeanDefinition());
        }
    }
}
