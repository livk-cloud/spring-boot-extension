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

package com.livk.context.useragent.reactive;

import com.livk.context.useragent.UserAgent;
import com.livk.context.useragent.UserAgentComposite;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author livk
 */
class ReactiveUserAgentFilterTests {

	@Test
	void filterWritesUserAgentIntoReactorContext() {
		UserAgentComposite composite = mock(UserAgentComposite.class);
		UserAgent userAgent = UserAgent.builder("Mozilla/5.0").browser("Chrome").build();
		given(composite.convert(any(HttpHeaders.class))).willReturn(userAgent);

		ReactiveUserAgentFilter filter = new ReactiveUserAgentFilter(composite);
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/"));
		WebFilterChain chain = (serverWebExchange) -> ReactiveUserAgentContextHolder.get()
			.doOnNext(ua -> assertThat(ua).isSameAs(userAgent))
			.then();

		StepVerifier.create(filter.filter(exchange, chain)).verifyComplete();
		verify(composite).convert(exchange.getRequest().getHeaders());
	}

	@Test
	void filterPropagatesChainError() {
		UserAgentComposite composite = mock(UserAgentComposite.class);
		given(composite.convert(any(HttpHeaders.class))).willReturn(UserAgent.builder("ua").browser("Chrome").build());

		ReactiveUserAgentFilter filter = new ReactiveUserAgentFilter(composite);
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/"));
		WebFilterChain chain = (serverWebExchange) -> Mono.error(new IllegalStateException("boom"));

		StepVerifier.create(filter.filter(exchange, chain))
			.expectErrorSatisfies(ex -> assertThat(ex).isInstanceOf(IllegalStateException.class).hasMessage("boom"))
			.verify();
	}

}
