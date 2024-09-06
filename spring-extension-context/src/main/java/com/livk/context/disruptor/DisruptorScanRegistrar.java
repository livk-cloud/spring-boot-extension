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

package com.livk.context.disruptor;

import com.livk.commons.util.AnnotationUtils;
import com.livk.commons.util.ObjectUtils;
import com.livk.context.disruptor.annotation.DisruptorEvent;
import com.livk.context.disruptor.exception.DisruptorRegistrarException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author livk
 */
public class DisruptorScanRegistrar implements ImportBeanDefinitionRegistrar {

	@Override
	public final void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata,
			@NonNull BeanDefinitionRegistry registry, @NonNull BeanNameGenerator beanNameGenerator) {
		String[] basePackages = getBasePackages(importingClassMetadata);
		if (ObjectUtils.isEmpty(basePackages)) {
			throw new DisruptorRegistrarException(
					DisruptorScan.class.getName() + " required basePackages or basePackageClasses");
		}
		ClassPathDisruptorScanner scanner = new ClassPathDisruptorScanner(registry, beanNameGenerator);
		scanner.registerFilters(DisruptorEvent.class);
		scanner.scan(basePackages);
	}

	protected String[] getBasePackages(AnnotationMetadata metadata) {
		AnnotationAttributes attributes = AnnotationUtils.attributesFor(metadata, DisruptorScan.class);
		String[] basePackages = attributes.getStringArray("basePackages");
		Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
		Set<String> packagesToScan = new LinkedHashSet<>(Arrays.asList(basePackages));
		for (Class<?> basePackageClass : basePackageClasses) {
			packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
		}
		if (packagesToScan.isEmpty()) {
			packagesToScan.add(ClassUtils.getPackageName(metadata.getClassName()));
		}
		return StringUtils.toStringArray(packagesToScan);
	}

}
