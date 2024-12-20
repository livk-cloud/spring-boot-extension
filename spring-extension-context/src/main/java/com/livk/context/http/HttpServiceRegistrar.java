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

import com.livk.commons.spring.AnnotationBasePackageSupport;
import com.livk.commons.util.ObjectUtils;
import com.livk.context.disruptor.DisruptorScan;
import com.livk.context.disruptor.exception.DisruptorRegistrarException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * <p>
 * HttpFactory
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public class HttpServiceRegistrar implements ImportBeanDefinitionRegistrar {

	private final Environment environment;

	@Override
	public final void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata,
			@NonNull BeanDefinitionRegistry registry) {
		String[] basePackages = getBasePackages(importingClassMetadata);
		if (ObjectUtils.isEmpty(basePackages)) {
			throw new DisruptorRegistrarException(
					DisruptorScan.class.getName() + " required basePackages or basePackageClasses");
		}
		ClassPathHttpScanner scanner = new ClassPathHttpScanner(registry);
		scanner.scan(basePackages);
	}

	protected String[] getBasePackages(AnnotationMetadata metadata) {
		return AnnotationBasePackageSupport.getBasePackages(metadata, HttpProviderScan.class);
	}

}
