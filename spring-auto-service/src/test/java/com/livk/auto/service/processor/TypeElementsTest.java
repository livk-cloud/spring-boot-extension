/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.auto.service.processor;

import com.google.auto.service.AutoService;
import com.livk.auto.service.processor.autoconfigure.SpringAutoContext;
import com.livk.auto.service.processor.autoconfigure.SpringContext;
import com.livk.auto.service.processor.factories.spring.SpringFactoryService;
import com.livk.auto.service.processor.factories.spring.SpringFactoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.annotation.MergedAnnotation;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * TypeElementsTest
 * </p>
 *
 * @author livk
 */
@ExtendWith(CompilationExtension.class)
class TypeElementsTest {

	@Test
	void getAnnotationAttributes(Elements elements) {
		Optional<Set<TypeElement>> autoServiceOption = TypeElements.getAnnotationAttributes(
				get(elements, SpringFactoryServiceImpl.class), AutoService.class, MergedAnnotation.VALUE);
		assertTrue(autoServiceOption.isPresent());
		ArrayList<TypeElement> list = autoServiceOption.map(ArrayList::new).get();
		assertFalse(list.isEmpty());
		assertEquals(1, list.size());
		assertEquals(SpringFactoryService.class.getName(), TypeElements.getBinaryName(list.getFirst()));
	}

	@Test
	void getBinaryName(Elements elements) {
		assertEquals(String.class.getName(), TypeElements.getBinaryName(get(elements, String.class)));
		assertEquals(SpringAutoContext.class.getName(),
				TypeElements.getBinaryName(get(elements, SpringAutoContext.class)));
		assertEquals(SpringContext.class.getName(), TypeElements.getBinaryName(get(elements, SpringContext.class)));
		assertEquals(SpringFactoryServiceImpl.class.getName(),
				TypeElements.getBinaryName(get(elements, SpringFactoryServiceImpl.class)));
	}

	private TypeElement get(Elements elements, Class<?> type) {
		return elements.getTypeElement(type.getCanonicalName());
	}

}
