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

package com.livk.commons.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class AnnotationMetadataResolverTests {

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner().withUserConfiguration(Config.class);

	@Test
	void find() {
		contextRunner.run(ctx -> {
			AnnotationMetadataResolver resolver = new AnnotationMetadataResolver(ctx);

			assertThat(resolver.find(TestAnnotation.class, AnnotationMetadataResolverTests.class.getPackageName()))
				.containsExactlyInAnyOrder(A.class, TestController.class);

			assertThat(resolver.find(TestAnnotation.class, ctx)).containsExactlyInAnyOrder(A.class,
					TestController.class);
		});
	}

	@TestConfiguration
	@AutoConfigurationPackage
	static class Config {

	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@interface TestAnnotation {

	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@TestAnnotation
	@Controller
	@interface TestController {

		@AliasFor(annotation = Controller.class)
		String value() default "";

	}

	@TestController
	static class A {

	}

}
