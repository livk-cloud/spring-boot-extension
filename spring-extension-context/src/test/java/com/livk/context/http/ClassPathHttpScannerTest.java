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

package com.livk.context.http;

import com.livk.context.http.factory.HttpFactoryBean;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class ClassPathHttpScannerTest {

	@Test
	void test() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		ClassPathHttpScanner scanner = new ClassPathHttpScanner(context);
		int result = scanner.scan(ClassPathHttpScannerTest.class.getPackageName());
		assertEquals(1, result);

		assertEquals(1, context.getBeanProvider(HttpService.class).stream().count());
	}

	@Test
	void testEmptyPackage() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		ClassPathHttpScanner scanner = new ClassPathHttpScanner(context);
		int result = scanner.scan(ClassPathHttpScannerTest.class.getPackageName() + ".empty");
		assertEquals(0, result);
		assertEquals(0, context.getBeanProvider(HttpService.class).stream().count());
		assertEquals(0, context.getBeanNamesForType(HttpFactoryBean.class).length);
	}

	@TestConfiguration
	static class Config {

	}

}
