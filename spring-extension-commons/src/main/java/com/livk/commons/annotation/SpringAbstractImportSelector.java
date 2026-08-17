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

package com.livk.commons.annotation;

import com.livk.commons.util.TypeUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Abstract import selector for importing Configuration or AutoConfiguration classes.
 * <p>
 * 具备识别 {@link AutoConfigureAfter}和{@link AutoConfigureBefore}的能力
 * <p>
 * 功效大致与 {@link AutoConfigurationImportSelector}一样，缺失排除自动装配的能力
 *
 * @param <A> 注解类型
 * @author livk
 * @see AutoConfigurationImportSelector
 * @see org.springframework.boot.autoconfigure.EnableAutoConfiguration
 */
public abstract class SpringAbstractImportSelector<A extends Annotation> extends AutoConfigurationImportSelector
		implements DeferredImportSelector {

	/**
	 * The Annotation class.
	 */
	private final Class<A> annotationClass;

	protected SpringAbstractImportSelector() {
		this.annotationClass = TypeUtils.resolveTypeArgument(this.getClass(), SpringAbstractImportSelector.class);
	}

	@Override
	protected @NonNull Class<A> getAnnotationClass() {
		return this.annotationClass;
	}

	@Override
	protected @NonNull List<String> getCandidateConfigurations(@NonNull AnnotationMetadata metadata,
			AnnotationAttributes attributes) {
		List<String> configurations = ImportCandidates.load(this.annotationClass, getBeanClassLoader()).getCandidates();
		Assert.notEmpty(configurations,
				"No auto configuration classes found in META-INF/spring/" + this.annotationClass.getName()
						+ ".imports. If you are using a custom packaging, make sure that file is correct.");
		return configurations;
	}

	@Override
	protected @NonNull List<String> getExcludeAutoConfigurationsProperty() {
		return Collections.emptyList();
	}

	@Override
	protected @NonNull Set<String> getExclusions(@NonNull AnnotationMetadata metadata,
			AnnotationAttributes attributes) {
		return Collections.emptySet();
	}

}
