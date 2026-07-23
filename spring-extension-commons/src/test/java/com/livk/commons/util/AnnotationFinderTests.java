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
class AnnotationFinderTests {

	private final Method method = AnnotationTestClass.class.getDeclaredMethod("parseMethod", String.class);

	private final MethodParameter methodParameter = new MethodParameter(method, 0);

	AnnotationFinderTests() throws NoSuchMethodException {
	}

	@Test
	void getAnnotationElementFromMethodParameterFindsMethodAnnotation() {
		assertThat(AnnotationFinder.getAnnotationElement(methodParameter, GetMapping.class)).isNotNull();
		assertThat(AnnotationFinder.getAnnotationElement(methodParameter, RequestMapping.class)).isNotNull();
	}

	@Test
	void getAnnotationElementFromMethodParameterFallsBackToClassAnnotation() {
		assertThat(AnnotationFinder.getAnnotationElement(methodParameter, Controller.class)).isNotNull();
		assertThat(AnnotationFinder.getAnnotationElement(methodParameter, RestController.class)).isNotNull();
	}

	@Test
	void getAnnotationElementFromMethodParameterReturnsNullWhenAbsent() {
		assertThat(AnnotationFinder.getAnnotationElement(methodParameter, RequestBody.class)).isNull();
	}

	@Test
	void getAnnotationElementFromMethodFindsMethodAnnotation() {
		assertThat(AnnotationFinder.getAnnotationElement(method, GetMapping.class)).isNotNull();
		assertThat(AnnotationFinder.getAnnotationElement(method, RequestMapping.class)).isNotNull();
	}

	@Test
	void getAnnotationElementFromMethodFallsBackToClassAnnotation() {
		assertThat(AnnotationFinder.getAnnotationElement(method, Controller.class)).isNotNull();
		assertThat(AnnotationFinder.getAnnotationElement(method, RestController.class)).isNotNull();
	}

	@Test
	void getAnnotationElementFromMethodReturnsNullWhenAbsent() {
		assertThat(AnnotationFinder.getAnnotationElement(method, RequestBody.class)).isNull();
	}

	@Test
	void hasAnnotationElementFromMethodParameterReturnsTrueForPresent() {
		assertThat(AnnotationFinder.hasAnnotationElement(methodParameter, RequestMapping.class)).isTrue();
		assertThat(AnnotationFinder.hasAnnotationElement(methodParameter, GetMapping.class)).isTrue();
		assertThat(AnnotationFinder.hasAnnotationElement(methodParameter, Controller.class)).isTrue();
		assertThat(AnnotationFinder.hasAnnotationElement(methodParameter, RestController.class)).isTrue();
	}

	@Test
	void hasAnnotationElementFromMethodParameterReturnsFalseForAbsent() {
		assertThat(AnnotationFinder.hasAnnotationElement(methodParameter, RequestBody.class)).isFalse();
	}

	@Test
	void hasAnnotationElementFromMethodReturnsTrueForPresent() {
		assertThat(AnnotationFinder.hasAnnotationElement(method, RequestMapping.class)).isTrue();
		assertThat(AnnotationFinder.hasAnnotationElement(method, Controller.class)).isTrue();
		assertThat(AnnotationFinder.hasAnnotationElement(method, RestController.class)).isTrue();
	}

	@Test
	void hasAnnotationElementFromMethodReturnsFalseForAbsent() {
		assertThat(AnnotationFinder.hasAnnotationElement(method, RequestBody.class)).isFalse();
	}

	@Test
	void attributesForWithClassReturnsAttributes() {
		AnnotationMetadata metadata = AnnotationMetadata.introspect(AnnotationTestClass.class);
		Set<MethodMetadata> methods = metadata.getAnnotatedMethods(RequestMapping.class.getName());
		assertThat(methods).isNotEmpty();
		for (MethodMetadata methodMetadata : methods) {
			AnnotationAttributes attributes = AnnotationFinder.attributesFor(methodMetadata, RequestMapping.class);
			assertThat(attributes).isNotNull();
		}
	}

	@Test
	void attributesForWithStringReturnsAttributes() {
		AnnotationMetadata metadata = AnnotationMetadata.introspect(AnnotationTestClass.class);
		Set<MethodMetadata> methods = metadata.getAnnotatedMethods(RequestMapping.class.getName());
		assertThat(methods).isNotEmpty();
		for (MethodMetadata methodMetadata : methods) {
			AnnotationAttributes attributes = AnnotationFinder.attributesFor(methodMetadata,
					RequestMapping.class.getName());
			assertThat(attributes).isNotNull();
		}
	}

	@Test
	void getValueExtractsEnumArray() {
		AnnotationMetadata metadata = AnnotationMetadata.introspect(AnnotationTestClass.class);
		Set<MethodMetadata> methods = metadata.getAnnotatedMethods(RequestMapping.class.getName());
		for (MethodMetadata methodMetadata : methods) {
			AnnotationAttributes attributes = AnnotationFinder.attributesFor(methodMetadata, RequestMapping.class);
			RequestMethod[] requestMethods = AnnotationFinder.getValue(attributes, "method");
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
