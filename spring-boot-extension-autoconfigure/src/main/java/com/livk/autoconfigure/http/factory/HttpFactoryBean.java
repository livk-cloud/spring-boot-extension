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

package com.livk.autoconfigure.http.factory;

import com.livk.commons.util.ClassUtils;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Arrays;
import java.util.Objects;

/**
 * <p>
 * HttpFactoryBean
 * </p>
 *
 * @author livk
 */
@Setter
public class HttpFactoryBean implements FactoryBean<Object>, BeanFactoryAware, ResourceLoaderAware {

	private String httpInterfaceTypeName;

	private BeanFactory beanFactory;

	private ResourceLoader resourceLoader;

	@Override
	public Object getObject() {
		HttpServiceProxyFactory proxyFactory = beanFactory.getBean(HttpServiceProxyFactory.class);
		return proxyFactory.createClient(Objects.requireNonNull(getObjectType()));
	}

	@Override
	public Class<?> getObjectType() {
		if (StringUtils.hasText(httpInterfaceTypeName)) {
			ClassLoader classLoader = resourceLoader == null ? ClassUtils.getDefaultClassLoader() : resourceLoader.getClassLoader();
			return ClassUtils.resolveClassName(httpInterfaceTypeName, classLoader);
		}
		return null;
	}
}
