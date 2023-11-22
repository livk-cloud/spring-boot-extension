/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.core.disruptor;

import com.livk.commons.util.AnnotationUtils;
import com.livk.core.disruptor.annotation.DisruptorEvent;
import com.livk.core.disruptor.factory.DisruptorFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The type Class path disruptor scanner.
 *
 * @author livk
 */
public class ClassPathDisruptorScanner extends ClassPathBeanDefinitionScanner {

	private final BeanNameGenerator beanNameGenerator;

	/**
	 * Instantiates a new Class path disruptor scanner.
	 * @param registry the registry
	 */
	public ClassPathDisruptorScanner(BeanDefinitionRegistry registry) {
		this(registry, new DefaultBeanNameGenerator());
	}

	/**
	 * Instantiates a new Class path disruptor scanner.
	 * @param registry the registry
	 * @param beanNameGenerator the bean name generator
	 */
	public ClassPathDisruptorScanner(BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator) {
		super(registry, false);
		this.beanNameGenerator = beanNameGenerator;
	}

	/**
	 * Register filters.
	 * @param annotationType the annotation type
	 */
	public void registerFilters(Class<? extends Annotation> annotationType) {
		addIncludeFilter(new AnnotationTypeFilter(annotationType));
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return beanDefinition.getMetadata().isIndependent() && !beanDefinition.getMetadata().isAnnotation();
	}

	@NonNull
	@Override
	protected Set<BeanDefinitionHolder> doScan(@NonNull String... basePackages) {
		Set<BeanDefinitionHolder> definitionHolders = new LinkedHashSet<>();
		BeanDefinitionRegistry registry = super.getRegistry();
		Assert.notNull(registry, "registry not be null");
		Assert.notEmpty(basePackages, "At least one base package must be specified");

		for (String basePackage : basePackages) {
			Set<BeanDefinition> candidateComponents = findCandidateComponents(basePackage);
			for (BeanDefinition candidateComponent : candidateComponents) {
				if (candidateComponent instanceof ScannedGenericBeanDefinition scannedGenericBeanDefinition) {
					AnnotationMetadata metadata = scannedGenericBeanDefinition.getMetadata();
					AnnotationAttributes attributes = AnnotationUtils.attributesFor(metadata, DisruptorEvent.class);
					String beanClassName = candidateComponent.getBeanClassName();
					Assert.notNull(beanClassName, "beanClassName not be null");
					Class<?> type = ClassUtils.resolveClassName(beanClassName,
							super.getResourceLoader().getClassLoader());
					BeanDefinitionBuilder builder = BeanDefinitionBuilder
						.genericBeanDefinition(DisruptorFactoryBean.class);
					builder.addPropertyValue("attributes", attributes);
					builder.addPropertyValue("type", type);
					builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

					AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
					String name = attributes.getString("value");
					String beanName = StringUtils.hasText(name) ? name
							: beanNameGenerator.generateBeanName(beanDefinition, registry);
					if (checkCandidate(beanName, beanDefinition)) {
						beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, beanClassName);

						BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, beanName);
						definitionHolders.add(holder);
						registerBeanDefinition(holder, registry);
					}
				}
			}
		}

		return definitionHolders;
	}

}
