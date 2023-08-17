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

package com.livk.commons.http;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.commons.http.annotation.EnableWebClient;
import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.DefaultSslContextSpec;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.function.Function;

/**
 * <p>
 * WebClientConfig
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
	 *
	 * @param builder the web client builder
	 * @return WebClient web client
	 */
	@Bean
	@ConditionalOnMissingBean
	public WebClient webClient(WebClient.Builder builder) {
		return builder.build();
	}

	/**
	 * The type Reactor client configuration.
	 */
	@AutoConfiguration
	@ConditionalOnClass(HttpClient.class)
	public static class ReactorClientConfiguration {

		/**
		 * Reactor client web client customizer web client customizer.
		 *
		 * @param reactorResourceFactory the reactor resource factory
		 * @return the web client customizer
		 */
		@Bean
		public WebClientCustomizer ReactorClientWebClientCustomizer(ReactorResourceFactory reactorResourceFactory) {
			Function<HttpClient, HttpClient> function = httpClient ->
				httpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3_000)
					.wiretap(WebClient.class.getName(), LogLevel.DEBUG,
						AdvancedByteBufFormat.TEXTUAL, StandardCharsets.UTF_8)
					.responseTimeout(Duration.ofSeconds(15))
					.secure(sslContextSpec -> sslContextSpec.sslContext(
						DefaultSslContextSpec.forClient().configure(builder ->
							builder.trustManager(InsecureTrustManagerFactory.INSTANCE))))
					.doOnConnected(connection ->
						connection.addHandlerLast(new ReadTimeoutHandler(20))
							.addHandlerLast(new WriteTimeoutHandler(20)));
			ReactorClientHttpConnector connector = new ReactorClientHttpConnector(reactorResourceFactory, function);
			return webClientBuilder -> webClientBuilder.clientConnector(connector);
		}
	}
}
