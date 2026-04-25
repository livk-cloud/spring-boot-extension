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
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class ReactiveUserAgentContextHolderTests {

	@Test
	void withContextAndGet() {
		UserAgent ua = UserAgent.builder("Mozilla/5.0").browser("Chrome").build();

		Mono<UserAgent> result = ReactiveUserAgentContextHolder.get()
			.contextWrite(ReactiveUserAgentContextHolder.withContext(ua));

		StepVerifier.create(result)
			.assertNext(agent -> assertThat(agent.browser()).isEqualTo("Chrome"))
			.verifyComplete();
	}

	@Test
	void withContextFromMono() {
		UserAgent ua = UserAgent.builder("Mozilla/5.0").browser("Firefox").build();

		Mono<UserAgent> result = ReactiveUserAgentContextHolder.get()
			.contextWrite(ReactiveUserAgentContextHolder.withContext(Mono.just(ua)));

		StepVerifier.create(result)
			.assertNext(agent -> assertThat(agent.browser()).isEqualTo("Firefox"))
			.verifyComplete();
	}

	@Test
	void getWithoutContextCompletesEmpty() {
		StepVerifier.create(ReactiveUserAgentContextHolder.get()).verifyComplete();
	}

	@Test
	void clearContextRemovesValue() {
		UserAgent ua = UserAgent.builder("Mozilla/5.0").build();

		Mono<UserAgent> result = ReactiveUserAgentContextHolder.get()
			.contextWrite(ReactiveUserAgentContextHolder.clearContext())
			.contextWrite(ReactiveUserAgentContextHolder.withContext(ua));

		StepVerifier.create(result).verifyComplete();
	}

	@Test
	void withContextReturnsReactorContext() {
		UserAgent ua = UserAgent.builder("test").build();
		Context ctx = ReactiveUserAgentContextHolder.withContext(ua);
		assertThat(ctx).isNotNull();
	}

}
