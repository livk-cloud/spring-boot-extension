/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.context.disruptor;

import com.livk.context.disruptor.factory.DisruptorFactoryBean;
import com.livk.context.disruptor.support.SpringDisruptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ResolvableType;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class ClassPathDisruptorScannerTest {

	@Test
	void test() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		ClassPathDisruptorScanner scanner = new ClassPathDisruptorScanner(context, DefaultBeanNameGenerator.INSTANCE);
		int result = scanner.scan(ClassPathDisruptorScannerTest.class.getPackageName());
		assertEquals(1, result);

		ResolvableType type = ResolvableType.forClassWithGenerics(SpringDisruptor.class, Entity.class);
		assertEquals(1, context.getBeanProvider(type).stream().count());
	}

	@Test
	void testEmptyPackage() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		ClassPathDisruptorScanner scanner = new ClassPathDisruptorScanner(context, DefaultBeanNameGenerator.INSTANCE);
		int result = scanner.scan(ClassPathDisruptorScannerTest.class.getPackageName() + ".empty");
		assertEquals(0, result);
		assertEquals(0, context.getBeanProvider(SpringDisruptor.class).stream().count());
		assertEquals(0, context.getBeanNamesForType(DisruptorFactoryBean.class).length);
	}

	@TestConfiguration
	static class Config {

	}

}
