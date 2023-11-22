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

package com.livk.commons.spring.context;

import lombok.Setter;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 导入普通的{@link Configuration}或者一些需要加入IOC的组件
 * <p>
 * 无法识别 {@link AutoConfigureAfter}和{@link AutoConfigureBefore}
 * <p>
 * 同时{@link AutoConfiguration}中仅仅识别{@link Configuration}
 *
 * @param <A> 注解泛型
 * @author livk
 * @see SpringAbstractImportSelector
 * @see DeferredImportSelector
 */
public abstract class AbstractImportSelector<A extends Annotation>
		implements DeferredImportSelector, Ordered, EnvironmentAware, BeanClassLoaderAware {

	/**
	 * 注解类型
	 */
	@SuppressWarnings("unchecked")
	protected final Class<A> annotationClass = (Class<A>) GenericTypeResolver.resolveTypeArgument(this.getClass(),
			AbstractImportSelector.class);

	/**
	 * The Environment.
	 */
	@Setter
	protected Environment environment;

	/**
	 * The Class loader.
	 */
	protected ClassLoader classLoader;

	@NonNull
	@Override
	public String[] selectImports(@Nullable AnnotationMetadata importingClassMetadata) {
		if (!isEnabled()) {
			return new String[0];
		}
		Assert.notNull(annotationClass, "annotation Class not be null");
		List<String> names = ImportCandidates.load(annotationClass, classLoader).getCandidates();
		return StringUtils.toStringArray(names);
	}

	/**
	 * 是否启用
	 * @return the boolean
	 */
	protected boolean isEnabled() {
		return true;
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

}
