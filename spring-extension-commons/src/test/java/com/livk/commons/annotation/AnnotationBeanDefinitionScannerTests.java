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

package com.livk.commons.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class AnnotationBeanDefinitionScannerTests {

	static final String CURRENT_PACKAGE = AnnotationBeanDefinitionScannerTests.class.getPackageName();

	@Test
	void scanRegistersAnnotatedBeanDefinition() {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		MyAnnotationBeanDefinitionScanner scanner = new MyAnnotationBeanDefinitionScanner(registry);
		scanner.scan(CURRENT_PACKAGE);

		assertThat(registry.containsBeanDefinition("annotationBeanDefinitionScannerTests.Config")).isTrue();
	}

	@Test
	void scanOnlyRegistersAnnotatedClasses() {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		MyAnnotationBeanDefinitionScanner scanner = new MyAnnotationBeanDefinitionScanner(registry);
		scanner.scan(CURRENT_PACKAGE);

		// Only Config is annotated with @MyAnnotationScanner, not UnannotatedClass
		assertThat(registry.containsBeanDefinition("annotationBeanDefinitionScannerTests.Config")).isTrue();
		for (String name : registry.getBeanDefinitionNames()) {
			assertThat(name).doesNotContainIgnoringCase("unannotated");
		}
	}

	@Test
	void scanWithUnrelatedPackageRegistersNothing() {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		MyAnnotationBeanDefinitionScanner scanner = new MyAnnotationBeanDefinitionScanner(registry);
		scanner.scan("com.livk.nonexistent");

		assertThat(registry.containsBeanDefinition("annotationBeanDefinitionScannerTests.Config")).isFalse();
	}

	@Test
	void generateHolderReturningNullSkipsRegistration() {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		NullHolderScanner scanner = new NullHolderScanner(registry);
		scanner.scan(CURRENT_PACKAGE);

		assertThat(registry.containsBeanDefinition("annotationBeanDefinitionScannerTests.Config")).isFalse();
	}

	@Test
	void scanWithCustomBeanNameGenerator() {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		BeanNameGenerator customGenerator = new AnnotationBeanNameGenerator();
		MyAnnotationBeanDefinitionScanner scanner = new MyAnnotationBeanDefinitionScanner(registry, customGenerator);
		scanner.scan(CURRENT_PACKAGE);

		assertThat(registry.containsBeanDefinition("annotationBeanDefinitionScannerTests.Config")).isTrue();
	}

	@Test
	void generateHolderReceivesAnnotationAttributes() {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		AttributeCapturingScanner scanner = new AttributeCapturingScanner(registry);
		scanner.scan(CURRENT_PACKAGE);

		assertThat(scanner.capturedAttributes).isNotNull();
	}

	static class MyAnnotationBeanDefinitionScanner extends AnnotationBeanDefinitionScanner<MyAnnotationScanner> {

		MyAnnotationBeanDefinitionScanner(BeanDefinitionRegistry registry) {
			super(registry);
		}

		MyAnnotationBeanDefinitionScanner(BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator) {
			super(registry, beanNameGenerator);
		}

		@Override
		protected BeanDefinitionHolder generateHolder(AnnotationAttributes attributes,
				BeanDefinition candidateComponent, BeanDefinitionRegistry registry) {
			String beanName = beanNameGenerator.generateBeanName(candidateComponent, registry);
			return new BeanDefinitionHolder(candidateComponent, beanName);
		}

	}

	static class NullHolderScanner extends AnnotationBeanDefinitionScanner<MyAnnotationScanner> {

		NullHolderScanner(BeanDefinitionRegistry registry) {
			super(registry);
		}

		@Override
		protected BeanDefinitionHolder generateHolder(AnnotationAttributes attributes,
				BeanDefinition candidateComponent, BeanDefinitionRegistry registry) {
			return null;
		}

	}

	static class AttributeCapturingScanner extends AnnotationBeanDefinitionScanner<MyAnnotationScanner> {

		AnnotationAttributes capturedAttributes;

		AttributeCapturingScanner(BeanDefinitionRegistry registry) {
			super(registry);
		}

		@Override
		protected BeanDefinitionHolder generateHolder(AnnotationAttributes attributes,
				BeanDefinition candidateComponent, BeanDefinitionRegistry registry) {
			this.capturedAttributes = attributes;
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

	@SuppressWarnings("unused")
	static class UnannotatedClass {

	}

}
