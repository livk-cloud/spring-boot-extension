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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class AnnotationBeanDefinitionScannerTests {

	@Test
	void test() {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		ClassPathBeanDefinitionScanner scanner = new MyAnnotationBeanDefinitionScanner(registry);
		scanner.scan("com.livk.commons.spring");
		assertTrue(registry.containsBeanDefinition("annotationBeanDefinitionScannerTests.Config"));
	}

	static class MyAnnotationBeanDefinitionScanner extends AnnotationBeanDefinitionScanner<MyAnnotationScanner> {

		public MyAnnotationBeanDefinitionScanner(BeanDefinitionRegistry registry) {
			super(registry);
		}

		@Override
		protected BeanDefinitionHolder generateHolder(AnnotationAttributes attributes,
				BeanDefinition candidateComponent, BeanDefinitionRegistry registry) {
			String beanName = beanNameGenerator.generateBeanName(candidateComponent, registry);
			return new BeanDefinitionHolder(candidateComponent, beanName);
		}

	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@interface MyAnnotationScanner {

	}

	@MyAnnotationScanner
	static class Config {

	}

}
