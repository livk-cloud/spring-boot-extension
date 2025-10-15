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
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class AnnotationBasePackageSupportTests {

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner().withUserConfiguration(Config.class);

	@Test
	void getBasePackages() {
		AnnotationMetadata metadata = AnnotationMetadata.introspect(Config.class);
		String[] basePackages = AnnotationBasePackageSupport.getBasePackages(metadata, AnnotationScan.class);
		assertThat(basePackages).containsExactly(AnnotationBasePackageSupportTests.class.getPackageName());

		contextRunner.run(context -> {
			String[] packages = AnnotationBasePackageSupport.getBasePackages(context);
			assertThat(packages).containsExactly(AnnotationBasePackageSupportTests.class.getPackageName());
		});
	}

	@TestConfiguration
	@AnnotationScan(basePackageClasses = Config.class)
	@AutoConfigurationPackage
	static class Config {

	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@interface AnnotationScan {

		@AliasFor("basePackages")
		String[] value() default {};

		@AliasFor(MergedAnnotation.VALUE)
		String[] basePackages() default {};

		Class<?>[] basePackageClasses() default {};

	}

}
