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

package com.livk.commons.util;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * AnnotationUtilsTest
 * </p>
 *
 * @author livk
 */
class AnnotationUtilsTest {

	private final Method method = AnnotationTestClass.class.getDeclaredMethod("parseMethod", String.class);

	private final MethodParameter methodParameter = new MethodParameter(method, 0);

	AnnotationUtilsTest() throws NoSuchMethodException {
	}

	@Test
	void getAnnotationElement() {
		assertNotNull(AnnotationUtils.getAnnotationElement(methodParameter, RequestMapping.class));
		assertNotNull(AnnotationUtils.getAnnotationElement(methodParameter, GetMapping.class));
		assertNotNull(AnnotationUtils.getAnnotationElement(methodParameter, Controller.class));
		assertNotNull(AnnotationUtils.getAnnotationElement(methodParameter, RestController.class));
		assertNull(AnnotationUtils.getAnnotationElement(methodParameter, RequestBody.class));

		assertNotNull(AnnotationUtils.getAnnotationElement(method, RequestMapping.class));
		assertNotNull(AnnotationUtils.getAnnotationElement(method, GetMapping.class));
		assertNotNull(AnnotationUtils.getAnnotationElement(method, Controller.class));
		assertNotNull(AnnotationUtils.getAnnotationElement(method, RestController.class));
		assertNull(AnnotationUtils.getAnnotationElement(method, RequestBody.class));
	}

	@Test
	void hasAnnotationElement() {
		assertTrue(AnnotationUtils.hasAnnotationElement(methodParameter, RequestMapping.class));
		assertTrue(AnnotationUtils.hasAnnotationElement(methodParameter, GetMapping.class));
		assertTrue(AnnotationUtils.hasAnnotationElement(methodParameter, Controller.class));
		assertTrue(AnnotationUtils.hasAnnotationElement(methodParameter, RestController.class));
		assertFalse(AnnotationUtils.hasAnnotationElement(methodParameter, RequestBody.class));

		assertTrue(AnnotationUtils.hasAnnotationElement(method, RequestMapping.class));
		assertTrue(AnnotationUtils.hasAnnotationElement(method, Controller.class));
		assertTrue(AnnotationUtils.hasAnnotationElement(method, RestController.class));
		assertFalse(AnnotationUtils.hasAnnotationElement(method, RequestBody.class));
	}

	@Test
	void test() {
		AnnotationMetadata annotationMetadata = AnnotationMetadata.introspect(AnnotationTestClass.class);
		Set<MethodMetadata> methods = annotationMetadata.getAnnotatedMethods(RequestMapping.class.getName());
		for (MethodMetadata metadata : methods) {
			AnnotationAttributes attributes = AnnotationUtils.attributesFor(metadata, RequestMapping.class);
			RequestMethod[] requestMethods = AnnotationUtils.getValue(attributes, "method");
			assertArrayEquals(new RequestMethod[] { RequestMethod.GET }, requestMethods);

			AnnotationAttributes attributesFor = AnnotationUtils.attributesFor(metadata,
					RequestMapping.class.getName());
			RequestMethod[] requestMethodsArr = AnnotationUtils.getValue(attributesFor, "method");
			assertArrayEquals(new RequestMethod[] { RequestMethod.GET }, requestMethodsArr);
		}
	}

	@RestController
	static class AnnotationTestClass {

		@GetMapping
		@SuppressWarnings("unused")
		private void parseMethod(String username) {
		}

	}

}
