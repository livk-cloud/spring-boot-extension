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

import com.livk.autoconfigure.http.HttpServiceProxyFactoryCustomizer;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.lang.NonNull;
import org.springframework.web.service.invoker.HttpExchangeAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * <p>
 * HttpFactoryBean
 * </p>
 *
 * @author livk
 */
@Setter
public class HttpFactoryBean implements FactoryBean<Object>, BeanFactoryAware {

	private BeanFactory beanFactory;

	private Class<?> type;

	private AdapterFactory<? extends HttpExchangeAdapter> adapterFactory;

	private ObjectProvider<HttpServiceProxyFactoryCustomizer> customizers;

	@Override
	public Object getObject() {
		HttpExchangeAdapter adapter = adapterFactory.create(beanFactory);
		HttpServiceProxyFactory.Builder builder = HttpServiceProxyFactory.builderFor(adapter);
		customizers.orderedStream().forEach(customizer -> customizer.customize(builder));
		HttpServiceProxyFactory proxyFactory = builder.build();
		return proxyFactory.createClient(type);
	}

	@Override
	public Class<?> getObjectType() {
		return type;
	}

	@Override
	public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
		customizers = beanFactory.getBeanProvider(HttpServiceProxyFactoryCustomizer.class);
	}

}
