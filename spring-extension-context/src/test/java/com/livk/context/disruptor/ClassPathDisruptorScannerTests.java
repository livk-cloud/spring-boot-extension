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

package com.livk.context.disruptor;

import com.livk.context.disruptor.factory.DisruptorFactoryBean;
import com.livk.context.disruptor.support.SpringDisruptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ResolvableType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class ClassPathDisruptorScannerTests {

	@Test
	void test() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		ClassPathDisruptorScanner scanner = new ClassPathDisruptorScanner(context, DefaultBeanNameGenerator.INSTANCE);
		int result = scanner.scan(ClassPathDisruptorScannerTests.class.getPackageName());
		assertThat(result).isEqualTo(1);

		ResolvableType type = ResolvableType.forClassWithGenerics(SpringDisruptor.class, Entity.class);
		assertThat(context.getBeanProvider(type)).hasSize(1);
	}

	@Test
	void testEmptyPackage() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		ClassPathDisruptorScanner scanner = new ClassPathDisruptorScanner(context, DefaultBeanNameGenerator.INSTANCE);
		int result = scanner.scan(ClassPathDisruptorScannerTests.class.getPackageName() + ".empty");
		assertThat(result).isEqualTo(0);
		assertThat(context.getBeanProvider(SpringDisruptor.class).stream()).isEmpty();
		assertThat(context.getBeanNamesForType(DisruptorFactoryBean.class)).isEmpty();
	}

	@TestConfiguration
	static class Config {

	}

}
