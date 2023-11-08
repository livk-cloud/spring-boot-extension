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

package com.livk.core.useragent.reactive;

import com.livk.core.useragent.domain.UserAgent;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.function.Function;

/**
 * The type Reactive user agent context holder.
 *
 * @author livk
 */
public class ReactiveUserAgentContextHolder {

	private static final Class<?> USER_AGENT_KEY = ReactiveUserAgentContextHolder.class;

	/**
	 * Get mono.
	 *
	 * @return the mono
	 */
	public static Mono<UserAgent> get() {
		return Mono.deferContextual(Mono::just)
			.cast(Context.class)
			.filter(ReactiveUserAgentContextHolder::hasContext)
			.flatMap(ReactiveUserAgentContextHolder::getContext);
	}

	private static boolean hasContext(Context context) {
		return context.hasKey(USER_AGENT_KEY);
	}

	private static Mono<UserAgent> getContext(Context context) {
		return context.<Mono<UserAgent>>get(USER_AGENT_KEY);
	}

	/**
	 * Clear context function.
	 *
	 * @return the function
	 */
	public static Function<Context, Context> clearContext() {
		return (context) -> context.delete(USER_AGENT_KEY);
	}

	/**
	 * With context context.
	 *
	 * @param useragentWrapper the useragent wrapper
	 * @return the context
	 */
	public static Context withContext(Mono<? extends UserAgent> useragentWrapper) {
		return Context.of(USER_AGENT_KEY, useragentWrapper);
	}

	/**
	 * With context context.
	 *
	 * @param userAgent the useragent wrapper
	 * @return the context
	 */
	public static Context withContext(UserAgent userAgent) {
		return withContext(Mono.just(userAgent));
	}
}
