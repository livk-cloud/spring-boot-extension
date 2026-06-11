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

package com.livk.auto.service.annotation;

import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class AutoServiceAnnotationTests {

	@Test
	void springAutoServiceMetadata() throws Exception {
		assertThat(SpringAutoService.class.getAnnotation(Retention.class).value()).isEqualTo(RetentionPolicy.SOURCE);
		assertThat(SpringAutoService.class.getAnnotation(Target.class).value()).containsExactly(ElementType.TYPE);
		assertThat(SpringAutoService.class.getDeclaredMethod("value").getDefaultValue())
			.isEqualTo(java.lang.annotation.Annotation.class);
	}

	@Test
	void springFactoriesMetadata() throws Exception {
		assertThat(SpringFactories.class.getAnnotation(Retention.class).value()).isEqualTo(RetentionPolicy.SOURCE);
		assertThat(SpringFactories.class.getAnnotation(Target.class).value()).containsExactly(ElementType.TYPE);
		assertThat(SpringFactories.class.getDeclaredMethod("value").getDefaultValue()).isEqualTo(Void.class);
	}

	@Test
	void aotFactoriesMetadata() throws Exception {
		assertThat(AotFactories.class.getAnnotation(Retention.class).value()).isEqualTo(RetentionPolicy.SOURCE);
		assertThat(AotFactories.class.getAnnotation(Target.class).value()).containsExactly(ElementType.TYPE);
		assertThat(AotFactories.class.getDeclaredMethod("value").getDefaultValue()).isEqualTo(Void.class);
	}

}
