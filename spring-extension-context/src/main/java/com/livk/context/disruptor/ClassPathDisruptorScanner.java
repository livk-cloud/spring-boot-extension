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

package com.livk.context.disruptor;

import com.livk.commons.spring.AnnotationBeanDefinitionScanner;
import com.livk.commons.util.BeanUtils;
import com.livk.commons.util.ClassUtils;
import com.livk.context.disruptor.annotation.DisruptorEvent;
import com.livk.context.disruptor.factory.DisruptorFactoryBean;
import com.livk.context.disruptor.support.SpringDisruptor;
import com.lmax.disruptor.WaitStrategy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.concurrent.ThreadFactory;

/**
 * The type Class path disruptor scanner.
 *
 * @author livk
 */
class ClassPathDisruptorScanner extends AnnotationBeanDefinitionScanner<DisruptorEvent> {

	/**
	 * Instantiates a new Class path disruptor scanner.
	 * @param registry the registry
	 * @param beanNameGenerator the bean name generator
	 */
	public ClassPathDisruptorScanner(BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator) {
		super(registry, beanNameGenerator);
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return beanDefinition.getMetadata().isIndependent() && !beanDefinition.getMetadata().isAnnotation();
	}

	@Override
	protected BeanDefinitionHolder generateHolder(AnnotationAttributes attributes, BeanDefinition candidateComponent,
			BeanDefinitionRegistry registry) {
		String beanClassName = candidateComponent.getBeanClassName();
		Assert.notNull(beanClassName, "beanClassName not be null");
		Class<?> type = ClassUtils.resolveClassName(beanClassName, super.getResourceLoader().getClassLoader());
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DisruptorFactoryBean.class)
			.addConstructorArgValue(type)
			.addPropertyValue("bufferSize", attributes.getNumber("bufferSize").intValue())
			.addPropertyValue("producerType", attributes.getEnum("type"))
			.addPropertyValue("threadFactory",
					BeanUtils.instantiateClass(attributes.<ThreadFactory>getClass("threadFactory")))
			.addPropertyValue("threadFactoryBeanName", attributes.getString("threadFactoryBeanName"))
			.addPropertyValue("useVirtualThreads", attributes.getBoolean("useVirtualThreads"))
			.addPropertyValue("waitStrategy", BeanUtils.instantiateClass(attributes.<WaitStrategy>getClass("strategy")))
			.addPropertyValue("strategyBeanName", attributes.getString("strategyBeanName"))
			.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		String name = attributes.getString(MergedAnnotation.VALUE);
		String beanName = StringUtils.hasText(name) ? name
				: beanNameGenerator.generateBeanName(beanDefinition, registry);
		if (checkCandidate(beanName, beanDefinition)) {
			ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SpringDisruptor.class, type);
			beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, resolvableType);

			return new BeanDefinitionHolder(beanDefinition, beanName);
		}
		return null;
	}

}
