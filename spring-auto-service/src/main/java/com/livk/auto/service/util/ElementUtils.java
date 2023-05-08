/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.auto.service.util;

import lombok.experimental.UtilityClass;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Element utils.
 *
 * @author livk
 */
@UtilityClass
public class ElementUtils {

    /**
     * Gets annotation attributes.
     *
     * @param <T>         the type parameter
     * @param element     the element
     * @param targetClass the target class
     * @param key         the key
     * @return the annotation attributes
     */
    public <T> Optional<TypeElement> getAnnotationAttributes(Element element, Class<T> targetClass, String key) {
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            TypeMirror typeMirror = annotationMirror.getAnnotationType().asElement().asType();
            if (typeMirror.toString().contentEquals(targetClass.getCanonicalName())) {
                Map<String, AnnotationValue> elementValues = annotationMirror.getElementValues()
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getValue() != null)
                        .collect(Collectors.toMap(entry -> entry.getKey().getSimpleName().toString(),
                                Map.Entry::getValue));
                return Optional.ofNullable(elementValues.get(key))
                        .map(annotationValue -> {
                            DeclaredType declaredType = (DeclaredType) annotationValue.getValue();
                            return (TypeElement) declaredType.asElement();
                        });
            }
        }
        throw new IllegalArgumentException(element + " not has annotation:" + targetClass);
    }

    /**
     * Gets binary name.
     *
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
