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

package com.livk.commons.spring;

import com.livk.commons.util.AnnotationUtils;
import com.livk.commons.util.ClassUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author livk
 */
@Slf4j
@UtilityClass
public class AnnotationBasePackageSupport {

	/**
	 * 从注解元数据中提取基础包路径。 支持从注解的 basePackages 和 basePackageClasses 属性获取，并以声明类的包路径作为默认值。
	 * @param metadata 注解元数据，通常来自类或方法的注解
	 * @param annotationClass 要解析的注解类型
	 * @return 去重后的基础包路径数组
	 */
	public String[] getBasePackages(AnnotationMetadata metadata, Class<? extends Annotation> annotationClass) {
		Set<String> packagesToScan = new LinkedHashSet<>();
		AnnotationAttributes attributes = AnnotationUtils.attributesFor(metadata, annotationClass);
		if (!CollectionUtils.isEmpty(attributes)) {
			if (attributes.containsKey("basePackages")) {
				String[] basePackages = attributes.getStringArray("basePackages");
				packagesToScan.addAll(Arrays.asList(basePackages));
				log.debug("Loaded basePackages from annotation: {}", packagesToScan);
			}
			if (attributes.containsKey("basePackageClasses")) {
				Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
				for (Class<?> basePackageClass : basePackageClasses) {
					if (basePackageClass != null) {
						String packageName = ClassUtils.getPackageName(basePackageClass);
						packagesToScan.add(packageName);
						log.debug("Added package from basePackageClasses: {}", packageName);
					}
				}
			}
			if (packagesToScan.isEmpty() && metadata != null && StringUtils.hasText(metadata.getClassName())) {
				String defaultPackage = ClassUtils.getPackageName(metadata.getClassName());
				packagesToScan.add(defaultPackage);
				log.debug("Using default package: {}", defaultPackage);
			}
		}
		return StringUtils.toStringArray(packagesToScan);
	}

	/**
	 * 从 Spring BeanFactory 中获取自动配置的基础包路径。 如果 BeanFactory 未配置自动包路径，则返回空数组。
	 * @param beanFactory spring 的 BeanFactory 实例
	 * @return 自动配置的基础包路径数组
	 */
	public String[] getBasePackages(BeanFactory beanFactory) {
		if (AutoConfigurationPackages.has(beanFactory)) {
			Set<String> packages = new LinkedHashSet<>(AutoConfigurationPackages.get(beanFactory));
			log.debug("Loaded base packages from BeanFactory: {}", packages);
			return StringUtils.toStringArray(packages);
		}
		log.debug("No base packages found in BeanFactory, returning empty array");
		return new String[0];
	}

}
