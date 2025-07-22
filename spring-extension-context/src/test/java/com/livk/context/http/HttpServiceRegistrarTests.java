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

package com.livk.context.http;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class HttpServiceRegistrarTests {

	@Test
	void registerBeanDefinitions() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

		ImportBeanDefinitionRegistrar registrar = new HttpServiceRegistrar();

		AnnotationMetadata metadata = AnnotationMetadata.introspect(HttpConfig.class);
		registrar.registerBeanDefinitions(metadata, context);

		assertEquals(1, context.getBeanProvider(HttpService.class).stream().count());
	}

	@TestConfiguration
	static class Config {

	}

}
