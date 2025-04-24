package com.livk.commons.util;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author livk
 */
class BeanLambdaDescriptorTest {

	@Test
	void create() {
		assertNotNull(BeanLambdaDescriptor.create(Maker::getNo));
	}

	@Test
	void getFieldName() {
		assertEquals("no", BeanLambdaDescriptor.create(Maker::getNo).getFieldName());
	}

	@Test
	void getField() throws NoSuchFieldException {
		Field field = Maker.class.getDeclaredField("no");
		assertEquals(field, BeanLambdaDescriptor.create(Maker::getNo).getField());
	}

	@Test
	void getMethodName() {
		assertEquals("getNo", BeanLambdaDescriptor.create(Maker::getNo).getMethodName());
	}

	@Test
	void getMethod() throws NoSuchMethodException {
		Method method = Maker.class.getMethod("getNo");
		assertEquals(method, BeanLambdaDescriptor.create(Maker::getNo).getMethod());
	}

	@Data
	static class Maker {

		private Integer no;

	}

}
