/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.filter.context;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.function.Function;

/**
 * <p>
 * ReactiveTenantContextHolder
 * </p>
 *
 * @author livk
 */
public abstract class ReactiveTenantContextHolder {

	public final static String ATTRIBUTES = "tenant";

	private static final Class<?> TENANT_ID_KEY = String.class;

	private static boolean hasContext(Context context) {
		return context.hasKey(TENANT_ID_KEY);
	}

	private static Mono<String> getContext(Context context) {
		return context.<Mono<String>>get(TENANT_ID_KEY);
	}

	public static Function<Context, Context> clearContext() {
		return (context) -> context.delete(TENANT_ID_KEY);
	}

	public static Context withContext(Mono<? extends String> tenantId) {
		return Context.of(TENANT_ID_KEY, tenantId);
	}

	public static Context withContext(String tenantId) {
		return withContext(Mono.just(tenantId));
	}

	public Mono<String> get() {
		return Mono.deferContextual(Mono::just)
			.cast(Context.class)
			.filter(ReactiveTenantContextHolder::hasContext)
			.flatMap(ReactiveTenantContextHolder::getContext);
	}

}
