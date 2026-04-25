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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class AnnotationUtilsTests {

	private final Method method = AnnotationTestClass.class.getDeclaredMethod("parseMethod", String.class);

	private final MethodParameter methodParameter = new MethodParameter(method, 0);

	AnnotationUtilsTests() throws NoSuchMethodException {
	}

	// --- getAnnotationElement(MethodParameter, Class) ---

	@Test
	void getAnnotationElementFromMethodParameterFindsMethodAnnotation() {
		assertThat(AnnotationUtils.getAnnotationElement(methodParameter, GetMapping.class)).isNotNull();
		assertThat(AnnotationUtils.getAnnotationElement(methodParameter, RequestMapping.class)).isNotNull();
	}

	@Test
	void getAnnotationElementFromMethodParameterFallsBackToClassAnnotation() {
		// @Controller and @RestController are on the class, not the method
		assertThat(AnnotationUtils.getAnnotationElement(methodParameter, Controller.class)).isNotNull();
		assertThat(AnnotationUtils.getAnnotationElement(methodParameter, RestController.class)).isNotNull();
	}

	@Test
	void getAnnotationElementFromMethodParameterReturnsNullWhenAbsent() {
		assertThat(AnnotationUtils.getAnnotationElement(methodParameter, RequestBody.class)).isNull();
	}

	// --- getAnnotationElement(Method, Class) ---

	@Test
	void getAnnotationElementFromMethodFindsMethodAnnotation() {
		assertThat(AnnotationUtils.getAnnotationElement(method, GetMapping.class)).isNotNull();
		assertThat(AnnotationUtils.getAnnotationElement(method, RequestMapping.class)).isNotNull();
	}

	@Test
	void getAnnotationElementFromMethodFallsBackToClassAnnotation() {
		assertThat(AnnotationUtils.getAnnotationElement(method, Controller.class)).isNotNull();
		assertThat(AnnotationUtils.getAnnotationElement(method, RestController.class)).isNotNull();
	}

	@Test
	void getAnnotationElementFromMethodReturnsNullWhenAbsent() {
		assertThat(AnnotationUtils.getAnnotationElement(method, RequestBody.class)).isNull();
	}

	// --- hasAnnotationElement(MethodParameter, Class) ---

	@Test
	void hasAnnotationElementFromMethodParameterReturnsTrueForPresent() {
		assertThat(AnnotationUtils.hasAnnotationElement(methodParameter, RequestMapping.class)).isTrue();
		assertThat(AnnotationUtils.hasAnnotationElement(methodParameter, GetMapping.class)).isTrue();
		assertThat(AnnotationUtils.hasAnnotationElement(methodParameter, Controller.class)).isTrue();
		assertThat(AnnotationUtils.hasAnnotationElement(methodParameter, RestController.class)).isTrue();
	}

	@Test
	void hasAnnotationElementFromMethodParameterReturnsFalseForAbsent() {
		assertThat(AnnotationUtils.hasAnnotationElement(methodParameter, RequestBody.class)).isFalse();
	}

	// --- hasAnnotationElement(Method, Class) ---

	@Test
	void hasAnnotationElementFromMethodReturnsTrueForPresent() {
		assertThat(AnnotationUtils.hasAnnotationElement(method, RequestMapping.class)).isTrue();
		assertThat(AnnotationUtils.hasAnnotationElement(method, Controller.class)).isTrue();
		assertThat(AnnotationUtils.hasAnnotationElement(method, RestController.class)).isTrue();
	}

	@Test
	void hasAnnotationElementFromMethodReturnsFalseForAbsent() {
		assertThat(AnnotationUtils.hasAnnotationElement(method, RequestBody.class)).isFalse();
	}

	// --- attributesFor ---

	@Test
	void attributesForWithClassReturnsAttributes() {
		AnnotationMetadata metadata = AnnotationMetadata.introspect(AnnotationTestClass.class);
		Set<MethodMetadata> methods = metadata.getAnnotatedMethods(RequestMapping.class.getName());
		assertThat(methods).isNotEmpty();
		for (MethodMetadata methodMetadata : methods) {
			AnnotationAttributes attributes = AnnotationUtils.attributesFor(methodMetadata, RequestMapping.class);
			assertThat(attributes).isNotNull();
		}
	}

	@Test
	void attributesForWithStringReturnsAttributes() {
		AnnotationMetadata metadata = AnnotationMetadata.introspect(AnnotationTestClass.class);
		Set<MethodMetadata> methods = metadata.getAnnotatedMethods(RequestMapping.class.getName());
		assertThat(methods).isNotEmpty();
		for (MethodMetadata methodMetadata : methods) {
			AnnotationAttributes attributes = AnnotationUtils.attributesFor(methodMetadata,
					RequestMapping.class.getName());
			assertThat(attributes).isNotNull();
		}
	}

	// --- getValue ---

	@Test
	void getValueExtractsEnumArray() {
		AnnotationMetadata metadata = AnnotationMetadata.introspect(AnnotationTestClass.class);
		Set<MethodMetadata> methods = metadata.getAnnotatedMethods(RequestMapping.class.getName());
		for (MethodMetadata methodMetadata : methods) {
			AnnotationAttributes attributes = AnnotationUtils.attributesFor(methodMetadata, RequestMapping.class);
			RequestMethod[] requestMethods = AnnotationUtils.getValue(attributes, "method");
			assertThat(requestMethods).containsExactly(RequestMethod.GET);
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
