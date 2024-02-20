/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.core.lock.intercept;

import com.livk.commons.aop.AnnotationAbstractPointcutTypeAdvisor;
import com.livk.commons.expression.ExpressionResolver;
import com.livk.commons.expression.spring.SpringExpressionResolver;
import com.livk.core.lock.DistributedLock;
import com.livk.core.lock.LockScope;
import com.livk.core.lock.annotation.OnLock;
import com.livk.core.lock.exception.LockException;
import com.livk.core.lock.exception.UnSupportLockException;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.Assert;

/**
 * <p>
 * LockAspect
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public class LockInterceptor extends AnnotationAbstractPointcutTypeAdvisor<OnLock> {

	/**
	 * lock的实现类集合
	 */
	private final ObjectProvider<DistributedLock> distributedLockProvider;

	/**
	 * SpEL表达式解析器
	 */
	private final ExpressionResolver resolver = new SpringExpressionResolver();

	@Override
	protected Object invoke(MethodInvocation invocation, OnLock onLock) throws Throwable {
		Assert.notNull(onLock, "lock is null");
		LockScope scope = onLock.scope();
		DistributedLock distributedLock = distributedLockProvider.orderedStream()
			.filter(lock -> lock.scope().equals(scope))
			.findFirst()
			.orElseThrow(() -> new UnSupportLockException("缺少scope：" + scope + "的锁实现"));
		boolean async = !LockScope.STANDALONE_LOCK.equals(scope) && onLock.async();
		String key = resolver.evaluate(onLock.key(), invocation.getMethod(), invocation.getArguments());
		boolean isLock = distributedLock.tryLock(onLock.type(), key, onLock.leaseTime(), onLock.waitTime(), async);
		try {
			if (isLock) {
				return invocation.proceed();
			}
			throw new LockException("获取锁失败!");
		}
		finally {
			if (isLock) {
				distributedLock.unlock();
			}
		}
	}

}
