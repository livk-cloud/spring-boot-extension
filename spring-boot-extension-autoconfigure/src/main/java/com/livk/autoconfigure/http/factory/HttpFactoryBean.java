/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.autoconfigure.http.factory;

import com.livk.commons.util.ClassUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.NonNull;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * <p>
 * HttpFactoryBean
 * </p>
 *
 * @author livk
 */
public class HttpFactoryBean implements FactoryBean<Object>, BeanFactoryAware {

	private BeanFactory beanFactory;

	private Class<?> type;

	/**
	 * Sets type.
	 * @param httpInterfaceTypeName the http interface type name
	 */
	public void setType(String httpInterfaceTypeName) {
		type = ClassUtils.resolveClassName(httpInterfaceTypeName);
	}

	@Override
	public Object getObject() {
		HttpServiceProxyFactory proxyFactory = beanFactory.getBean(HttpServiceProxyFactory.class);
		return proxyFactory.createClient(type);
	}

	@Override
	public Class<?> getObjectType() {
		return type;
	}

	@Override
	public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
