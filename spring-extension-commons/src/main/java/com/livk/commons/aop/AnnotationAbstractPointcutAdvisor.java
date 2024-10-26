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

package com.livk.commons.aop;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 注解型切点处理器
 *
 * @param <A> 注解
 * @author livk
 */
public abstract class AnnotationAbstractPointcutAdvisor<A extends Annotation> extends AbstractPointcutAdvisor
		implements IntroductionInterceptor {

	/**
	 * 切点注解类型
	 */
	@SuppressWarnings("unchecked")
	protected final Class<A> annotationType = (Class<A>) GenericTypeResolver.resolveTypeArgument(this.getClass(),
			AnnotationAbstractPointcutAdvisor.class);

	@NonNull
	@Override
	public Object invoke(@NonNull MethodInvocation invocation) throws Throwable {
		Assert.notNull(annotationType, "annotationType must not be null");
		Method method = invocation.getMethod();
		AnnotationTarget<A> target = AnnotationTarget.of(annotationType);
		A annotation = target.getAnnotation(method);
		if (annotation == null && invocation.getThis() != null) {
			annotation = target.getAnnotation(invocation.getThis().getClass());
		}
		return invoke(invocation, annotation);
	}

	@Override
	public int getOrder() {
		int order = super.getOrder();
		return order == Ordered.LOWEST_PRECEDENCE ? Ordered.LOWEST_PRECEDENCE - 1 : order;
	}

	/**
	 * 执行拦截的方法
	 * @param invocation 方法相关信息
	 * @param annotation 注解信息
	 * @return 方法返回结果 object
	 * @throws Throwable the throwable
	 */
	protected abstract Object invoke(MethodInvocation invocation, A annotation) throws Throwable;

	@Override
	public boolean implementsInterface(@NonNull Class<?> intf) {
		return annotationType != null && annotationType.isAssignableFrom(intf);
	}

	@NonNull
	@Override
	public Advice getAdvice() {
		return this;
	}

}
