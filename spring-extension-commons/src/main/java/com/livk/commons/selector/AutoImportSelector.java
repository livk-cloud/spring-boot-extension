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

package com.livk.commons.selector;

import com.livk.commons.util.ClassUtils;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 使用注解的方式代替 XXXImportSelector
 * <p>
 * 减少XXXImportSelector继承 {@link SpringAbstractImportSelector}却不重写方法
 *
 * @author livk
 * @see AutoImport
 */
class AutoImportSelector extends SpringAbstractImportSelector<AutoImport> {

	@Override
	protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
		Set<String> names = new HashSet<>();
		if (annotationClass != null) {
			for (String annotationType : metadata.getAnnotationTypes()) {
				Class<?> type = ClassUtils.resolveClassName(annotationType, getBeanClassLoader());
				if (type.isAnnotation() && type.isAnnotationPresent(annotationClass)) {
					names.addAll(ImportCandidates.load(type, getBeanClassLoader()).getCandidates());
				}
			}
		}
		return new ArrayList<>(names);
	}

}
