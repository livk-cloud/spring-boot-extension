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

import com.livk.commons.aop.AnnotationAbstractPointcutTypeAdvisor;
import com.livk.commons.expression.ExpressionResolver;
import com.livk.commons.expression.spring.SpringExpressionResolver;
import com.livk.context.lock.DistributedLock;
import com.livk.context.lock.annotation.DistLock;
import com.livk.context.lock.exception.LockException;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.Assert;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class DistributedLockInterceptor extends AnnotationAbstractPointcutTypeAdvisor<DistLock> {

	/**
	 * lock的实现类集合
	 */
	private final ObjectProvider<DistributedLock> distributedLockProvider;

	/**
	 * SpEL表达式解析器
	 */
	private final ExpressionResolver resolver = new SpringExpressionResolver();

	@Override
	protected Object invoke(MethodInvocation invocation, DistLock lock) throws Throwable {
		Assert.notNull(lock, "lock is null");
		DistributedLock distributedLock = distributedLockProvider.orderedStream()
			.findFirst()
			.orElseThrow(() -> new NoSuchBeanDefinitionException(DistributedLock.class));
		String key = resolver.evaluate(lock.key(), invocation.getMethod(), invocation.getArguments());
		boolean isLock = distributedLock.tryLock(lock.type(), key, lock.leaseTime(), lock.waitTime(), lock.async());
		try {
			if (isLock) {
				return invocation.proceed();
			}
			throw new LockException("Failed to acquire locks!");
		}
		finally {
			if (isLock) {
				distributedLock.unlock();
			}
		}
	}

}
