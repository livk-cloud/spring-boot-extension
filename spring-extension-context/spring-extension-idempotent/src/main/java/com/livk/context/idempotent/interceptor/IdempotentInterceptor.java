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

package com.livk.context.idempotent.interceptor;

import com.livk.commons.aop.AbstractAnnotationPointcutStrategyAdvisor;
import com.livk.commons.expression.ExpressionResolver;
import com.livk.commons.expression.spring.SpringExpressionResolver;
import com.livk.context.idempotent.IdempotentExecutor;
import com.livk.context.idempotent.annotation.Idempotent;
import com.livk.context.idempotent.exception.IdempotentException;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * AOP interceptor that enforces {@link Idempotent} by occupying a token before the method
 * runs.
 *
 * @author livk
 * @see Idempotent
 * @see IdempotentExecutor
 */
@RequiredArgsConstructor
public class IdempotentInterceptor extends AbstractAnnotationPointcutStrategyAdvisor<Idempotent> {

	private final ObjectProvider<IdempotentExecutor> executors;

	private final ExpressionResolver resolver = new SpringExpressionResolver();

	private volatile IdempotentExecutor executor;

	@Override
	protected Object doInvoke(MethodInvocation invocation, Idempotent idempotent) throws Throwable {
		String key = this.resolver.resolve(idempotent.key())
			.method(invocation.getMethod(), invocation.getArguments())
			.evaluate();
		if (!StringUtils.hasText(key)) {
			throw new IdempotentException("Idempotent key must not be empty");
		}
		Duration ttl = toDuration(idempotent.ttl(), idempotent.ttlUnit());
		IdempotentExecutor current = getExecutor();
		if (!current.tryAcquire(key, ttl)) {
			throw new IdempotentException("Duplicate request: " + key);
		}
		try {
			return invocation.proceed();
		}
		catch (Throwable ex) {
			if (idempotent.releaseOnError()) {
				releaseQuietly(current, key, ex);
			}
			throw ex;
		}
	}

	private IdempotentExecutor getExecutor() {
		IdempotentExecutor result = this.executor;
		if (result == null) {
			synchronized (this) {
				result = this.executor;
				if (result == null) {
					result = this.executors.orderedStream()
						.findFirst()
						.orElseThrow(() -> new NoSuchBeanDefinitionException(IdempotentExecutor.class));
					this.executor = result;
				}
			}
		}
		return result;
	}

	private static Duration toDuration(long ttl, TimeUnit unit) {
		if (ttl <= 0) {
			throw new IdempotentException("Idempotent ttl must be positive");
		}
		return Duration.ofMillis(unit.toMillis(ttl));
	}

	private static void releaseQuietly(IdempotentExecutor executor, String key, Throwable ex) {
		try {
			executor.release(key);
		}
		catch (RuntimeException releaseEx) {
			ex.addSuppressed(releaseEx);
		}
	}

}
