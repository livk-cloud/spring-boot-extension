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
