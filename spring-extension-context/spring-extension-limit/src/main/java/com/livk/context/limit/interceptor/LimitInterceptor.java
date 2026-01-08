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

package com.livk.context.limit.interceptor;

import com.livk.commons.aop.AnnotationAbstractPointcutTypeAdvisor;
import com.livk.commons.expression.ExpressionResolver;
import com.livk.commons.expression.spring.SpringExpressionResolver;
import com.livk.commons.util.BeanUtils;
import com.livk.commons.util.HttpServletUtils;
import com.livk.context.limit.LimitExecutor;
import com.livk.context.limit.annotation.Limit;
import com.livk.context.limit.exception.LimitExceededHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * The type Limit interceptor.
 *
 * @author livk
 */
@Slf4j
@RequiredArgsConstructor
public class LimitInterceptor extends AnnotationAbstractPointcutTypeAdvisor<Limit> {

	/**
	 * 执行器
	 */
	private final ObjectProvider<LimitExecutor> providers;

	private final ExpressionResolver resolver = new SpringExpressionResolver();

	@Override
	protected Object invoke(MethodInvocation invocation, Limit limit) throws Throwable {
		String key = limit.key();
		int rate = limit.rate();
		int rateInterval = limit.rateInterval();
		TimeUnit unit = limit.rateIntervalUnit();
		String spELKey = resolver.evaluate(key, invocation.getMethod(), invocation.getArguments());
		if (limit.restrictIp()) {
			String ip = HttpServletUtils.realIp(HttpServletUtils.request());
			spELKey = spELKey + "#" + ip;
		}
		LimitExecutor executor = providers.orderedStream()
			.findFirst()
			.orElseThrow(() -> new NoSuchBeanDefinitionException(LimitExecutor.class));
		boolean status = executor.tryAccess(spELKey, rate, Duration.ofMillis(unit.toMillis(rateInterval)));
		if (status) {
			return invocation.proceed();
		}
		else {
			Class<? extends LimitExceededHandler> handlerType = limit.handler();
			LimitExceededHandler handler = handlerType == LimitExceededHandler.class ? LimitExceededHandler.DEFAULT
					: BeanUtils.instantiateClass(handlerType);
			throw handler.buildException(limit);
		}
	}

}
