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

package com.livk.auto.service.processor;

import com.google.auto.service.AutoService;
import com.livk.auto.service.processor.autoconfigure.SpringAutoContext;
import com.livk.auto.service.processor.autoconfigure.SpringContext;
import com.livk.auto.service.processor.factories.spring.SpringFactoryService;
import com.livk.auto.service.processor.factories.spring.SpringFactoryServiceImpl;
import com.sun.source.util.JavacTask;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.MergedAnnotation;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class TypeElementsTests {

	static Elements elements;

	@BeforeAll
	static void init() throws IOException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {

			JavaFileObject file = new SimpleJavaFileObject(URI.create("string:///Dummy.java"),
					JavaFileObject.Kind.SOURCE) {
				@Override
				public CharSequence getCharContent(boolean ignoreEncodingErrors) {
					return "final class Dummy {}";
				}
			};

			JavacTask task = (JavacTask) compiler.getTask(null, fileManager, null, List.of("-proc:none"), null,
					List.of(file));

			task.parse();
			task.analyze();

			elements = task.getElements();
		}
	}

	@Test
	void getAnnotationAttributes() {
		Optional<Set<TypeElement>> autoServiceOption = TypeElements
			.getAnnotationAttributes(get(SpringFactoryServiceImpl.class), AutoService.class, MergedAnnotation.VALUE);
		assertThat(autoServiceOption).isPresent().map(ArrayList::new).hasValueSatisfying(list -> {
			assertThat(list).isNotEmpty().hasSize(1);
			assertThat(TypeElements.getBinaryName(list.getFirst())).isEqualTo(SpringFactoryService.class.getName());
		});
	}

	@Test
	void getBinaryName() {
		List<Class<?>> classes = List.of(String.class, SpringAutoContext.class, SpringContext.class,
				SpringFactoryServiceImpl.class);

		for (Class<?> type : classes) {
			assertThat(TypeElements.getBinaryName(get(type))).isEqualTo(type.getName());
		}

	}

	static TypeElement get(Class<?> type) {
		return elements.getTypeElement(type.getCanonicalName());
	}

}
