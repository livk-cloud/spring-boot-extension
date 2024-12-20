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

package com.livk.context.http;

import com.livk.commons.spring.AnnotationBeanDefinitionScanner;
import com.livk.commons.util.ClassUtils;
import com.livk.context.http.annotation.HttpProvider;
import com.livk.context.http.exception.HttpServiceRegistrarException;
import com.livk.context.http.factory.AdapterFactory;
import com.livk.context.http.factory.AdapterType;
import com.livk.context.http.factory.HttpFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.Assert;
import org.springframework.web.service.invoker.HttpExchangeAdapter;

import java.util.Comparator;
import java.util.List;

/**
 * The type Class path http scanner.
 *
 * @author livk
 */
public class ClassPathHttpScanner extends AnnotationBeanDefinitionScanner<HttpProvider> {

	/**
	 * Instantiates a new Class path http scanner.
	 * @param registry the registry
	 */
	public ClassPathHttpScanner(BeanDefinitionRegistry registry) {
		super(registry);
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return beanDefinition.getMetadata().isIndependent() && !beanDefinition.getMetadata().isAnnotation();
	}

	@Override
	protected BeanDefinitionHolder generateHolder(AnnotationAttributes attributes, BeanDefinition candidateComponent,
			BeanDefinitionRegistry registry) {
		AdapterType type = attributes.getEnum("type");
		AdapterFactory<? extends HttpExchangeAdapter> adapterFactory = this.getAdapterFactory(type);
		String beanClassName = candidateComponent.getBeanClassName();
		Assert.notNull(beanClassName, "beanClassName not be null");
		Class<?> beanType = ClassUtils.resolveClassName(beanClassName, super.getResourceLoader().getClassLoader());
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(HttpFactoryBean.class);
		builder.addPropertyValue("type", beanType);
		builder.addPropertyValue("adapterFactory", adapterFactory);
		builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		String beanName = beanNameGenerator.generateBeanName(candidateComponent, registry);
		if (checkCandidate(beanName, beanDefinition)) {
			beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, beanType);
			return new BeanDefinitionHolder(beanDefinition, beanName);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	protected AdapterFactory<? extends HttpExchangeAdapter> getAdapterFactory(AdapterType type) {
		List<AdapterFactory> adapterFactories = SpringFactoriesLoader.loadFactories(AdapterFactory.class,
				getResourceLoader().getClassLoader());
		adapterFactories.sort(Comparator.comparingInt(AdapterFactory::getOrder));
		for (AdapterFactory adapterFactory : adapterFactories) {
			if (type == AdapterType.AUTO && adapterFactory.support() || adapterFactory.type() == type) {
				return (AdapterFactory<? extends HttpExchangeAdapter>) adapterFactory;
			}
		}
		throw new HttpServiceRegistrarException("adapterFactory not be found");
	}

}
