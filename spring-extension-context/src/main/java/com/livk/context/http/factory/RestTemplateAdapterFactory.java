/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
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

package com.livk.context.http.factory;

import com.livk.auto.service.annotation.SpringFactories;
import com.livk.commons.util.ClassUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestTemplateAdapter;

import java.util.function.Supplier;

/**
 * @author livk
 */
@SpringFactories
@Deprecated(since = "1.4.3", forRemoval = true)
class RestTemplateAdapterFactory implements AdapterFactory<RestTemplateAdapter> {

	@Override
	public boolean support() {
		return ClassUtils.isPresent("org.springframework.web.client.RestTemplate");
	}

	@Override
	public RestTemplateAdapter create(BeanFactory beanFactory) {
		ObjectProvider<RestTemplate> beanProvider = beanFactory.getBeanProvider(RestTemplate.class);
		Supplier<RestTemplate> defaultSupplier = RestTemplate::new;
		if (ClassUtils.isPresent("org.springframework.boot.web.client.RestTemplateBuilder")) {
			RestTemplateBuilder builder = beanFactory.getBeanProvider(RestTemplateBuilder.class)
				.getIfUnique(RestTemplateBuilder::new);
			defaultSupplier = builder::build;
		}
		RestTemplate restTemplate = beanProvider.getIfUnique(defaultSupplier);
		return RestTemplateAdapter.create(restTemplate);
	}

	@Override
	public AdapterType type() {
		return AdapterType.REST_TEMPLATE;
	}

	@Override
	public int getOrder() {
		return AdapterFactory.super.getOrder() + 2;
	}

}
