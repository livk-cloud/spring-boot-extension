/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.commons.spring;

import com.livk.commons.util.AnnotationUtils;
import com.livk.commons.util.ClassUtils;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author livk
 */
@UtilityClass
public class AnnotationBasePackageSupport {

	public String[] getBasePackages(AnnotationMetadata metadata, Class<? extends Annotation> annotationClass) {
		AnnotationAttributes attributes = AnnotationUtils.attributesFor(metadata, annotationClass);
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

	public String[] getBasePackages(BeanFactory beanFactory) {
		if (AutoConfigurationPackages.has(beanFactory)) {
			return StringUtils.toStringArray(AutoConfigurationPackages.get(beanFactory));
		}
		return new String[0];
	}

}
