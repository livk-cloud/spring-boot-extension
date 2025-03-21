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

import com.google.auto.common.MoreTypes;
import lombok.experimental.UtilityClass;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor14;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Element utils.
 *
 * @author livk
 */
@UtilityClass
class TypeElements {

	/**
	 * Gets annotation attributes.
	 * @param <T> the type parameter
	 * @param element the element
	 * @param targetClass the target class
	 * @param key the key
	 * @return the annotation attributes
	 */
	public <T> Optional<Set<TypeElement>> getAnnotationAttributes(Element element, Class<T> targetClass, String key) {
		for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
			TypeMirror typeMirror = annotationMirror.getAnnotationType().asElement().asType();
			if (typeMirror.toString().contentEquals(targetClass.getCanonicalName())) {
				for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror
					.getElementValues()
					.entrySet()) {
					if (entry.getKey().getSimpleName().contentEquals(key)) {
						return Optional.of(entry.getValue().accept(new SimpleAnnotationValueVisitor(), null));
					}
				}
				return Optional.empty();
			}
		}
		throw new IllegalArgumentException(element + " not has annotation:" + targetClass);
	}

	private static class SimpleAnnotationValueVisitor extends SimpleAnnotationValueVisitor14<Set<TypeElement>, Void> {

		@Override
		public Set<TypeElement> visitType(TypeMirror t, Void v) {
			return Set.of(MoreTypes.asTypeElement(t));
		}

		@Override
		public Set<TypeElement> visitArray(List<? extends AnnotationValue> values, Void unused) {
			return values.stream()
				.flatMap(value -> value.accept(this, null).stream())
				.collect(Collectors.toUnmodifiableSet());
		}

	}

	/**
	 * Gets binary name.
	 * @param element the element
	 * @return the binary name
	 */
	public String getBinaryName(TypeElement element) {
		return getBinaryNameImpl(element, element.getSimpleName().toString());
	}

	private String getBinaryNameImpl(TypeElement element, String className) {
		Element enclosingElement = element.getEnclosingElement();

		if (enclosingElement instanceof PackageElement pkg) {
			if (pkg.isUnnamed()) {
				return className;
			}
			return pkg.getQualifiedName() + "." + className;
		}
		return getBinaryNameImpl((TypeElement) enclosingElement, enclosingElement.getSimpleName() + "$" + className);
	}

}
