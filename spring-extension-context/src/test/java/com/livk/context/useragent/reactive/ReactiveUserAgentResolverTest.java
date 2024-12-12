/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.useragent.reactive;

import com.livk.context.useragent.UserAgentConverter;
import com.livk.context.useragent.UserAgentHelper;
import com.livk.context.useragent.annotation.UserAgentInfo;
import com.livk.context.useragent.domain.UserAgent;
import com.livk.context.useragent.yauaa.YauaaUserAgentConverter;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.reactive.BindingContext;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoOperator;
import reactor.test.StepVerifier;

import java.lang.reflect.Method;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
@SpringJUnitConfig(ReactiveUserAgentResolverTest.WebFluxConfig.class)
class ReactiveUserAgentResolverTest {

	final ReactiveUserAgentResolver resolver;

	private BindingContext bindContext;

	@BeforeEach
	void setup() {
		ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
		initializer.setConversionService(new DefaultFormattingConversionService());
		this.bindContext = new BindingContext(initializer);
	}

	@Autowired
	ReactiveUserAgentResolverTest(UserAgentHelper helper) {
		this.resolver = new ReactiveUserAgentResolver(helper);
	}

	@Test
	void supportsParameter() throws Exception {
		Method method = this.getClass().getDeclaredMethod("test", Mono.class);
		MethodParameter parameter = MethodParameter.forExecutable(method, 0);

		assertTrue(resolver.supportsParameter(parameter));
	}

	@Test
	void resolveArgument() throws Exception {
		StepVerifier.create(ReactiveUserAgentContextHolder.get()).expectNextCount(0).verifyComplete();

		Method method = this.getClass().getDeclaredMethod("test", Mono.class);
		MethodParameter parameter = MethodParameter.forExecutable(method, 0);

		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/path?name=foo")
			.header(HttpHeaders.USER_AGENT,
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"));

		Mono<Object> mono = resolver.resolveArgument(parameter, bindContext, exchange);

		Mono<UserAgent> result = mono.map(o -> {
			if (o instanceof MonoOperator<?, ?> operator) {
				return Mono.from(operator);
			}
			return Mono.justOrEmpty(o);
		}).flatMap(Function.identity()).cast(UserAgent.class);

		verifyComplete(result.map(UserAgent::userAgentStr),
				exchange.getRequest().getHeaders().getFirst(HttpHeaders.USER_AGENT));

		verifyComplete(result.map(UserAgent::browser), "Chrome");
		verifyComplete(result.map(UserAgent::browserType), "Browser");
		verifyComplete(result.map(UserAgent::browserVersion), "120");
		verifyComplete(result.map(UserAgent::os), "Windows NT");
		verifyComplete(result.map(UserAgent::osVersion), "Windows NT ??");
		verifyComplete(result.map(UserAgent::deviceType), "Desktop");
		verifyComplete(result.map(UserAgent::deviceName), "Desktop");

		verifyComplete(result.mapNotNull(UserAgent::deviceBrand).defaultIfEmpty("empty"), "Unknown");
	}

	<T> void verifyComplete(Publisher<T> publisher, T next) {
		StepVerifier.create(publisher).expectNext(next).verifyComplete();
	}

	@SuppressWarnings("unused")
	void test(@UserAgentInfo Mono<UserAgent> mono) {
	}

	@Configuration
	static class WebFluxConfig {

		@Bean
		public UserAgentConverter yauaaUserAgentConverter() {
			UserAgentAnalyzer analyzer = UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(10).build();
			return new YauaaUserAgentConverter(analyzer);
		}

		@Bean
		public UserAgentHelper userAgentHelper() {
			return new UserAgentHelper();
		}

	}

}
