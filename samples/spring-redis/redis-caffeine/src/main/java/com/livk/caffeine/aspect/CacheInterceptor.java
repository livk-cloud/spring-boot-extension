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

package com.livk.caffeine.aspect;

import com.livk.caffeine.annotation.DoubleCache;
import com.livk.commons.aop.AnnotationAbstractPointcutTypeAdvisor;
import com.livk.commons.expression.ExpressionResolver;
import com.livk.commons.expression.spring.SpringExpressionResolver;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;

/**
 * @author livk
 */
@Component
public class CacheInterceptor extends AnnotationAbstractPointcutTypeAdvisor<DoubleCache> {

	private final Cache cache;

	private final ExpressionResolver resolver = new SpringExpressionResolver();

	public CacheInterceptor(CacheManager cacheManager) {
		this.cache = cacheManager.getCache("redis-caffeine");
	}

	@Override
	protected Object invoke(MethodInvocation invocation, DoubleCache doubleCache) throws Throwable {
		Assert.notNull(doubleCache, "doubleCache is null");
		String spELResult = resolver.evaluate(doubleCache.key(), invocation.getMethod(), invocation.getArguments());
		String realKey = doubleCache.cacheName() + ":" + spELResult;
		switch (doubleCache.type()) {
			case FULL -> {
				return cache.get(realKey, call(invocation.proceed()));
			}
			case PUT -> {
				Object proceed = invocation.proceed();
				cache.put(realKey, proceed);
				return proceed;
			}
			case DELETE -> {
				Object proceed = invocation.proceed();
				cache.evict(realKey);
				return proceed;
			}
		}
		return invocation.proceed();
	}

	private Callable<Object> call(Object obj) {
		return () -> obj;
	}

}
