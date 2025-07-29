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

package com.livk.context.http.factory;

import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.jspecify.annotations.NonNull;
import org.springframework.web.service.invoker.HttpExchangeAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * @author livk
 */
@Setter
public class HttpFactoryBean implements FactoryBean<Object>, BeanFactoryAware, InitializingBean {

	private ConfigurableBeanFactory beanFactory;

	private Class<?> type;

	private AdapterFactory adapterFactory;

	private Object httpClient;

	@Override
	public Object getObject() {
		return httpClient;
	}

	@Override
	public void afterPropertiesSet() {
		HttpExchangeAdapter adapter = adapterFactory.create(beanFactory);
		HttpServiceProxyFactory.Builder builder = HttpServiceProxyFactory.builderFor(adapter);
		builder.embeddedValueResolver(new EmbeddedValueResolver(beanFactory));
		HttpServiceProxyFactory proxyFactory = builder.build();
		httpClient = proxyFactory.createClient(type);
	}

	@Override
	public Class<?> getObjectType() {
		return type;
	}

	@Override
	public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (ConfigurableBeanFactory) beanFactory;
	}

}
