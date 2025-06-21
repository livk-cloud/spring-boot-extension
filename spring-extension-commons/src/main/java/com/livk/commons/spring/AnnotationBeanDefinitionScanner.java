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

package com.livk.commons.spring;

import com.livk.commons.util.AnnotationUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author livk
 */
public abstract class AnnotationBeanDefinitionScanner<T extends Annotation> extends ClassPathBeanDefinitionScanner {

	@SuppressWarnings("unchecked")
	protected final Class<T> annotationClass = Objects.requireNonNull(
			(Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), AnnotationBeanDefinitionScanner.class));

	protected final BeanNameGenerator beanNameGenerator;

	protected AnnotationBeanDefinitionScanner(BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator) {
		super(registry, false);
		this.beanNameGenerator = beanNameGenerator;
		addIncludeFilter(new AnnotationTypeFilter(annotationClass));
	}

	protected AnnotationBeanDefinitionScanner(BeanDefinitionRegistry registry) {
		this(registry, new AnnotationBeanNameGenerator());
	}

	@NonNull
	@Override
	protected final Set<BeanDefinitionHolder> doScan(@NonNull String... basePackages) {
		BeanDefinitionRegistry registry = super.getRegistry();
		Assert.notNull(registry, "registry not be null");
		Assert.notEmpty(basePackages, "At least one base package must be specified");
		Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<>();
		for (String basePackage : basePackages) {
			Set<BeanDefinition> candidateComponents = findCandidateComponents(basePackage);
			for (BeanDefinition candidateComponent : candidateComponents) {
				if (candidateComponent instanceof ScannedGenericBeanDefinition scannedGenericBeanDefinition) {
					AnnotationAttributes attributes = AnnotationUtils
						.attributesFor(scannedGenericBeanDefinition.getMetadata(), annotationClass);
					BeanDefinitionHolder holder = generateHolder(attributes, candidateComponent, registry);
					if (holder != null) {
						beanDefinitions.add(holder);
						registerBeanDefinition(holder, registry);
					}
				}
			}
		}
		return beanDefinitions;
	}

	protected abstract BeanDefinitionHolder generateHolder(AnnotationAttributes attributes,
			BeanDefinition candidateComponent, BeanDefinitionRegistry registry);

}
