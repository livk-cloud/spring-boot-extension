/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.commons.http;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.commons.http.annotation.EnableWebClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.reactive.function.client.ReactorNettyHttpClientMapper;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ReactorResourceFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * WebClient相关装配
 * </p>
 *
 * @author livk
 */
@AutoConfiguration(after = WebClientAutoConfiguration.class)
@ConditionalOnClass(WebClient.class)
@SpringAutoService(EnableWebClient.class)
public class WebClientConfiguration {

	/**
	 * spring官方建议使用{@link WebClient} <a href=
	 * "https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#webmvc-client">Spring文档</a>
	 * @param builder the web client builder
	 * @return WebClient web client
	 */
	@Bean
	@ConditionalOnMissingBean
	public WebClient webClient(WebClient.Builder builder) {
		return builder.build();
	}

	/**
	 * Reactor client配置
	 */
	@AutoConfiguration
	@ConditionalOnClass(HttpClient.class)
	public static class ReactorClientConfiguration {

		@Bean
		@Lazy
		@Order(0)
		public WebClientCustomizer clientConnectorCustomizer(ReactorResourceFactory reactorResourceFactory,
				ObjectProvider<ReactorNettyHttpClientMapper> mapperProvider) {
			return builder -> {
				List<ReactorNettyHttpClientMapper> mappers = mapperProvider.orderedStream()
					.collect(Collectors.toCollection(ArrayList::new));
				builder.clientConnector(new ReactorClientHttpConnector(reactorResourceFactory,
						ReactorNettyHttpClientMapper.of(mappers)::configure));
			};
		}

		@Bean
		@ConditionalOnMissingBean
		public ReactorResourceFactory reactorClientResourceFactory() {
			ReactorResourceFactory factory = new ReactorResourceFactory();
			factory.setUseGlobalResources(false);
			return factory;
		}

		@Bean
		public ReactorNettyHttpClientMapper reactorNettyHttpClientMapper() {
			return new DefaultReactorNettyHttpClientMapper();
		}

	}

}
