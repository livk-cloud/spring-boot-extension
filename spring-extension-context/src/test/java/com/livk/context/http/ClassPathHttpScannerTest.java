package com.livk.context.http;

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

	@TestConfiguration
	static class Config {

	}

}
