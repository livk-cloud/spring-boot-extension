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
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;

/**
 * @author livk
 */
@SpringFactories
class RestClientAdapterFactory implements AdapterFactory<RestClientAdapter> {

	@Override
	public boolean support() {
		return ClassUtils.isPresent("org.springframework.web.client.RestClient");
	}

	@Override
	public RestClientAdapter create(BeanFactory beanFactory) {
		RestClient restClient = beanFactory.getBeanProvider(RestClient.class).getIfUnique();
		if (restClient == null) {
			RestClient.Builder builder = beanFactory.getBeanProvider(RestClient.Builder.class)
				.getIfUnique(RestClient::builder);
			restClient = builder.build();
		}
		return RestClientAdapter.create(restClient);
	}

	@Override
	public AdapterType type() {
		return AdapterType.REST_CLIENT;
	}

	@Override
	public int getOrder() {
		return AdapterFactory.super.getOrder() + 1;
	}

}
