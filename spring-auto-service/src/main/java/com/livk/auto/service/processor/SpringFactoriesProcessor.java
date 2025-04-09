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
import com.livk.auto.service.annotation.SpringFactories;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Optional;
import java.util.Set;

/**
 * <p>
 * SpringFactoriesProcessor
 * </p>
 *
 * @author livk
 */
@AutoService(Processor.class)
public class SpringFactoriesProcessor extends AbstractFactoriesProcessor {

	private static final Class<SpringFactories> SUPPORT_CLASS = SpringFactories.class;

	static final String SPRING_LOCATION = "META-INF/spring.factories";

	protected SpringFactoriesProcessor() {
		super(SPRING_LOCATION);
	}

	@Override
	protected Set<Class<?>> getSupportedAnnotation() {
		return Set.of(SUPPORT_CLASS);
	}

	@Override
	protected Set<? extends Element> getElements(RoundEnvironment roundEnv) {
		return roundEnv.getElementsAnnotatedWith(SUPPORT_CLASS);
	}

	@Override
	protected Optional<Set<TypeElement>> getAnnotationAttributes(Element element) {
		return TypeElements.getAnnotationAttributes(element, SUPPORT_CLASS, VALUE);
	}

}
