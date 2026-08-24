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

package com.livk.context.lock.intercept;

import com.livk.commons.aop.AbstractAnnotationPointcutStrategyAdvisor;
import com.livk.commons.expression.ExpressionResolver;
import com.livk.commons.expression.spring.SpringExpressionResolver;
import com.livk.commons.util.Applier;
import com.livk.context.lock.DistLockFactory;
import com.livk.context.lock.annotation.DistLock;
import com.livk.context.lock.exception.LockException;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.Assert;

/**
 * AOP interceptor for distributed locks that intercepts methods annotated with
 * {@link DistLock} and automatically performs lock/unlock operations.
 * <p>
 * This interceptor extends {@link AbstractAnnotationPointcutStrategyAdvisor} to
 * automatically detect {@link DistLock} annotations, parse annotation parameters, and
 * execute distributed lock operations through {@link DistLockFactory}.
 * <p>
 * Workflow:
 * <ol>
 * <li>Obtain the highest-priority {@link DistLockFactory} instance from the Spring
 * container</li>
 * <li>Resolve the actual lock key from {@link DistLock#key()} using SpEL expression
 * parsing</li>
 * <li>Configure lock spec based on annotation parameters (lease time, wait time, async
 * mode)</li>
 * <li>Attempt to acquire the lock; on success execute the target method, on failure throw
 * {@link LockException}</li>
 * <li>Release the lock after method execution (regardless of success or exception)</li>
 * </ol>
 *
 * @author livk
 * @see DistLock
 * @see DistLockFactory
 * @see AbstractAnnotationPointcutStrategyAdvisor
 */
@RequiredArgsConstructor
public class DistributedLockInterceptor extends AbstractAnnotationPointcutStrategyAdvisor<DistLock> {

	/**
	 * Ordered provider of distributed lock factories for selecting the highest-priority
	 * factory instance.
	 */
	private final ObjectProvider<DistLockFactory> distLockFactories;

	/**
	 * SpEL expression resolver for parsing expressions in {@link DistLock#key()}.
	 */
	private final ExpressionResolver resolver = new SpringExpressionResolver();

	/**
	 * Execute the distributed lock interception logic.
	 * <p>
	 * Parses annotation parameters, acquires the lock, executes the target method, and
	 * releases the lock afterward. Throws {@link LockException} if lock acquisition
	 * fails.
	 * @param invocation the method invocation containing target method and argument info
	 * @param lock the {@link DistLock} annotation instance on the target method
	 * @return the return value of the target method
	 * @throws Throwable any exception thrown by the target method
	 * @throws LockException if lock acquisition fails
	 */
	@Override
	protected Object doInvoke(MethodInvocation invocation, DistLock lock) throws Throwable {
		Assert.notNull(lock, "lock is null");
		DistLockFactory distLockFactory = this.distLockFactories.orderedStream()
			.findFirst()
			.orElseThrow(() -> new NoSuchBeanDefinitionException(DistLockFactory.class));
		String key = this.resolver.resolve(lock.key())
			.method(invocation.getMethod(), invocation.getArguments())
			.evaluate();
		DistLockFactory.SpecLock specLock = distLockFactory.lock(key, lock.type());
		Applier.of(lock.leaseTime()).ge(0L).apply(specLock::leaseTime);
		Applier.of(lock.waitTime()).ge(0L).apply(specLock::waitTime);
		Applier.of(lock.async()).isTrue().apply(specLock::async);
		boolean isLock = specLock.tryLock();
		try {
			if (isLock) {
				return invocation.proceed();
			}
			throw new LockException("Failed to acquire locks!");
		}
		finally {
			if (isLock) {
				specLock.unlock();
			}
		}
	}

}
