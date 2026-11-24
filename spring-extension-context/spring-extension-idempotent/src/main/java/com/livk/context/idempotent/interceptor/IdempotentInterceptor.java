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
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * AOP interceptor that enforces {@link Idempotent} by occupying a token before the method
 * runs.
 * <p>
 * Only externally invoked, proxy-interceptable methods are covered. Private methods and
 * same-instance self-invocations bypass Spring AOP and therefore skip idempotent checks.
 * {@link Idempotent#releaseOnError()} applies to synchronous exceptions and to
 * {@link CompletionStage} results that complete exceptionally.
 *
 * @author livk
 * @see Idempotent
 * @see IdempotentExecutor
 */
@Slf4j
@RequiredArgsConstructor
public class IdempotentInterceptor extends AbstractAnnotationPointcutStrategyAdvisor<Idempotent> {

	private final ObjectProvider<IdempotentExecutor> executors;

	private final ExpressionResolver resolver = new SpringExpressionResolver();

	private volatile IdempotentExecutor executor;

	@Override
	protected Object doInvoke(MethodInvocation invocation, Idempotent idempotent) throws Throwable {
		Method method = resolveMethod(invocation);
		String key = this.resolver.resolve(idempotent.key()).method(method, invocation.getArguments()).evaluate();
		if (!StringUtils.hasText(key)) {
			throw new IdempotentException("Idempotent key must not be empty");
		}
		Duration ttl = toDuration(idempotent.ttl(), idempotent.ttlUnit());
		IdempotentExecutor current = getExecutor();
		String token = current.tryAcquire(key, ttl);
		if (token == null) {
			throw new IdempotentException("Duplicate request: " + key);
		}
		try {
			Object result = invocation.proceed();
			if (idempotent.releaseOnError() && result instanceof CompletionStage<?> stage) {
				stage.whenComplete((ignored, error) -> {
					if (error != null) {
						releaseQuietly(current, key, token, error);
					}
				});
			}
			return result;
		}
		catch (Throwable ex) {
			if (idempotent.releaseOnError()) {
				releaseQuietly(current, key, token, ex);
			}
			throw ex;
		}
	}

	private static Method resolveMethod(MethodInvocation invocation) {
		Method method = invocation.getMethod();
		Object target = invocation.getThis();
		if (target != null) {
			return AopUtils.getMostSpecificMethod(method, AopUtils.getTargetClass(target));
		}
		return method;
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
		try {
			Duration duration = Duration.of(ttl, unit.toChronoUnit());
			if (duration.isZero() || duration.isNegative()) {
				throw new IdempotentException("Idempotent ttl must be positive");
			}
			return duration;
		}
		catch (ArithmeticException ex) {
			throw new IdempotentException("Idempotent ttl is too large", ex);
		}
	}

	private static void releaseQuietly(IdempotentExecutor executor, String key, String token, Throwable ex) {
		try {
			executor.release(key, token);
		}
		catch (RuntimeException releaseEx) {
			log.warn("Failed to release idempotent token for key '{}'", key, releaseEx);
			ex.addSuppressed(releaseEx);
		}
	}

}
