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

package com.livk.context.useragent.reactive;

import com.livk.context.useragent.UserAgent;
import com.livk.context.useragent.UserAgentHelper;
import com.livk.context.useragent.annotation.UserAgentInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.jspecify.annotations.NonNull;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>
 * AbstractUserAgentHandlerMethodArgumentResolver
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public class ReactiveUserAgentResolver implements HandlerMethodArgumentResolver {

	private final ReactiveAdapterRegistry adapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

	private final UserAgentHelper helper;

	@Override
	public final boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(UserAgentInfo.class);
	}

	@NonNull
	@Override
	public final Mono<Object> resolveArgument(@NonNull MethodParameter parameter,
			@NonNull BindingContext bindingContext, ServerWebExchange exchange) {
		Class<?> resolvedType = ResolvableType.forMethodParameter(parameter).resolve();
		ReactiveAdapter adapter = (resolvedType != null ? adapterRegistry.getAdapter(resolvedType) : null);

		Mono<UserAgent> mono = ReactiveUserAgentContextHolder.get()
			.switchIfEmpty(Mono.justOrEmpty(helper.convert(exchange.getRequest().getHeaders())));
		return (adapter != null ? Mono.just(adapter.fromPublisher(mono)) : Mono.from(mono));
	}

}
