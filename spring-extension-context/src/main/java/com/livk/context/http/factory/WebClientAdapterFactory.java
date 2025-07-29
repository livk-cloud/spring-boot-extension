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

import com.livk.auto.service.annotation.SpringFactories;
import com.livk.commons.util.ClassUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;

/**
 * @author livk
 */
@SpringFactories
class WebClientAdapterFactory implements AdapterFactory {

	@Override
	public boolean support() {
		return ClassUtils.isPresent("org.springframework.web.reactive.function.client.WebClient");
	}

	@Override
	public WebClientAdapter create(BeanFactory beanFactory) {
		WebClient client = beanFactory.getBeanProvider(WebClient.class).getIfUnique();
		if (client == null) {
			WebClient.Builder builder = beanFactory.getBeanProvider(WebClient.Builder.class)
				.getIfUnique(WebClient::builder);
			client = builder.build();
		}
		return WebClientAdapter.create(client);
	}

	@Override
	public AdapterType type() {
		return AdapterType.WEB_CLIENT;
	}

}
