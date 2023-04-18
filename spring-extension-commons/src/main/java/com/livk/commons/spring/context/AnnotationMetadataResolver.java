/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.commons.spring.context;

import com.livk.commons.util.AnnotationUtils;
import com.livk.commons.util.ObjectUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Annotation metadata resolver.
 *
 * @author livk
 */
public class AnnotationMetadataResolver {

    private final ResourceLoader resourceLoader;

    private final ResourcePatternResolver resolver;

    private final MetadataReaderFactory metadataReaderFactory;

    /**
     * Instantiates a new Annotation metadata resolver.
     *
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
     *
     * @param annotationType 注解
     * @param packages       待扫描的包
     * @return set class
     */
    public Set<Class<?>> find(Class<? extends Annotation> annotationType, String... packages) {
        Set<Class<?>> typeSet = new HashSet<>();
        if (ObjectUtils.isEmpty(packages)) {
            return typeSet;
        }
        for (String packageStr : packages) {
            packageStr = packageStr.replace(".", "/");
            try {
                Resource[] resources = resolver.getResources("classpath*:" + packageStr + "/**/*.class");
                for (Resource resource : resources) {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    String className = metadataReader.getClassMetadata().getClassName();
                    Class<?> type = ClassUtils.forName(className, resourceLoader.getClassLoader());
                    if (AnnotationUtils.isAnnotationDeclaredLocally(annotationType, type) ||
                        AnnotatedElementUtils.hasAnnotation(type, annotationType)) {
                        typeSet.add(type);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return typeSet;
    }

    /**
     * 获取被注解标注的class
     *
     * @param annotationType 注解
     * @param beanFactory    springboot包扫描
     * @return set class
     */
    public Set<Class<?>> find(Class<? extends Annotation> annotationType, BeanFactory beanFactory) {
        return find(annotationType, StringUtils.toStringArray(AutoConfigurationPackages.get(beanFactory)));
    }
}
