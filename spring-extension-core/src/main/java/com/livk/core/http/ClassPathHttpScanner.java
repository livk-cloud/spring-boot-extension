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

package com.livk.core.http;

import com.livk.core.http.factory.HttpFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The type Class path http scanner.
 *
 * @author livk
 */
public class ClassPathHttpScanner extends ClassPathBeanDefinitionScanner {

	private final BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

	/**
	 * Instantiates a new Class path http scanner.
	 *
	 * @param registry    the registry
	 * @param environment the environment
	 */
	public ClassPathHttpScanner(BeanDefinitionRegistry registry, Environment environment) {
		super(registry, false, environment);
	}

	/**
	 * Register filters.
	 *
	 * @param annotationType the annotation type
	 */
	public void registerFilters(Class<? extends Annotation> annotationType) {
		addIncludeFilter(new AnnotationTypeFilter(annotationType));
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return beanDefinition.getMetadata().isIndependent() && !beanDefinition.getMetadata().isAnnotation();
	}

	@Override
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		BeanDefinitionRegistry registry = super.getRegistry();
		Assert.notNull(registry, "registry not be null");
		Assert.notEmpty(basePackages, "At least one base package must be specified");
		Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<>();
		for (String basePackage : basePackages) {
			Set<BeanDefinition> candidateComponents = findCandidateComponents(basePackage);
			for (BeanDefinition candidateComponent : candidateComponents) {
				String beanClassName = candidateComponent.getBeanClassName();
				BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(HttpFactoryBean.class);
				builder.addPropertyValue("type", beanClassName);
				builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

				AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
				String beanName = beanNameGenerator.generateBeanName(candidateComponent, registry);
				if (checkCandidate(beanName, beanDefinition)) {
					beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, beanClassName);

					BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, beanName);
					registerBeanDefinition(holder, registry);
				}
			}
		}
		return beanDefinitions;
	}
}
