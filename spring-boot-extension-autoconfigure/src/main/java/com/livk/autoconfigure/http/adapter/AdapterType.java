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

package com.livk.autoconfigure.http.adapter;

import com.livk.commons.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.client.support.RestTemplateAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpExchangeAdapter;

/**
 * @author livk
 */
@RequiredArgsConstructor
public enum AdapterType {

	REST_TEMPLATE,

	WEB_CLIENT,

	REST_CLIENT,

	/**
	 * 适配器模式自动检测，出现多个情况，默认顺序为
	 * {@link WebClient} > {@link RestClient} > {@link RestTemplate}
	 */
	AUTO;

	@SuppressWarnings("unchecked")
	public static <T extends AdapterFactory<? extends HttpExchangeAdapter>> T builder(AdapterType type) {
		return switch (type) {
			case REST_TEMPLATE -> (T) new RestTemplateAdapterWrapper();
			case WEB_CLIENT -> (T) new WebClientAdapterWrapper();
			case REST_CLIENT -> (T) new RestClientAdapterWrapper();
			case AUTO -> {
				ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
				if (ClassUtils.isPresent("org.springframework.web.reactive.function.client.WebClient", classLoader)) {
					yield (T) new WebClientAdapterWrapper();
				} else if (ClassUtils.isPresent("org.springframework.web.client.RestClient", classLoader)) {
					yield (T) new RestClientAdapterWrapper();
				} else if (ClassUtils.isPresent("org.springframework.web.client.RestTemplate", classLoader)) {
					yield (T) new RestTemplateAdapterWrapper();
				}
				throw new UnsupportedOperationException("缺少构建HttpExchangeAdapter的类信息");
			}
		};
	}


	private static class RestTemplateAdapterWrapper implements AdapterFactory<RestTemplateAdapter> {

		@Override
		public RestTemplateAdapter create(BeanFactory beanFactory) {
			RestTemplate restTemplate = beanFactory.getBeanProvider(RestTemplate.class)
				.getIfAvailable(() -> beanFactory.getBean(RestTemplateBuilder.class).build());
			return RestTemplateAdapter.create(restTemplate);
		}
	}

	private static class WebClientAdapterWrapper implements AdapterFactory<WebClientAdapter> {

		@Override
		public WebClientAdapter create(BeanFactory beanFactory) {
			WebClient webClient = beanFactory.getBeanProvider(WebClient.class)
				.getIfAvailable(() -> beanFactory.getBean(WebClient.Builder.class).build());
			return WebClientAdapter.create(webClient);
		}
	}

	private static class RestClientAdapterWrapper implements AdapterFactory<RestClientAdapter> {

		@Override
		public RestClientAdapter create(BeanFactory beanFactory) {
			RestClient restClient = beanFactory.getBeanProvider(RestClient.class)
				.getIfAvailable(() -> beanFactory.getBean(RestClient.Builder.class).build());
			return RestClientAdapter.create(restClient);
		}
	}
}
