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

package com.livk.commons.util;

import lombok.Getter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * 包解析根据Annotation进行class获取
 *
 * @author livk
 * @see org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
 */
public class AnnotationMetadataResolver {

	@Getter
	private final ResourceLoader resourceLoader;

	private final ResourcePatternResolver resolver;

	private final MetadataReaderFactory metadataReaderFactory;

	/**
	 * Instantiates a new Annotation metadata resolver.
	 * @param resourceLoader the resource loader
	 */
	public AnnotationMetadataResolver(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
		this.resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
	}

	/**
	 * Instantiates a new Annotation metadata resolver.
	 */
	public AnnotationMetadataResolver() {
		this(new DefaultResourceLoader());
	}

	/**
	 * 获取被注解标注的class
	 * @param annotationType 注解
	 * @param packages 待扫描的包
	 * @return set class
	 */
	public Set<Class<?>> find(Class<? extends Annotation> annotationType, String... packages) {
		TypeFilter typeFilter = new AnnotationTypeFilter(annotationType);
		return find(typeFilter, packages);
	}

	/**
	 * 获取被注解标注的class
	 * @param annotationType 注解
	 * @param beanFactory beanFactory
	 * @return set class
	 */
	public Set<Class<?>> find(Class<? extends Annotation> annotationType, BeanFactory beanFactory) {
		return find(annotationType, StringUtils.toStringArray(AutoConfigurationPackages.get(beanFactory)));
	}

	/**
	 * 获取被满足条件的的class
	 * @param typeFilter type匹配器
	 * @param packages 待扫描的包
	 * @return set class
	 */
	public Set<Class<?>> find(TypeFilter typeFilter, String... packages) {
		Set<Class<?>> result = new HashSet<>();
		if (ObjectUtils.isEmpty(packages)) {
			return result;
		}
		for (String packageStr : packages) {
			packageStr = ClassUtils.convertClassNameToResourcePath(packageStr);
			try {
				Resource[] resources = resolver
					.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + packageStr + "/**/*.class");
				for (Resource resource : resources) {
					MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
					if (typeFilter.match(metadataReader, metadataReaderFactory)) {
						Class<?> type = toType(metadataReader);
						result.add(type);
					}
				}
			}
			catch (IOException e) {
				throw new ResourceAccessException("Failed to read metadata", e);
			}
		}
		return result;
	}

	private Class<?> toType(MetadataReader metadataReader) {
		String className = metadataReader.getClassMetadata().getClassName();
		return ClassUtils.resolveClassName(className, resourceLoader.getClassLoader());
	}

}
