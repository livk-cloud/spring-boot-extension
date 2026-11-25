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

package com.livk.context.sequence.interceptor;

import com.livk.commons.aop.AbstractAnnotationPointcutStrategyAdvisor;
import com.livk.commons.expression.ExpressionResolver;
import com.livk.commons.expression.spring.SpringExpressionResolver;
import com.livk.context.sequence.Sequence;
import com.livk.context.sequence.SequenceProvider;
import com.livk.context.sequence.annotation.Seq;
import com.livk.context.sequence.exception.SequenceException;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.StringUtils;

import java.lang.reflect.Parameter;

/**
 * AOP interceptor that fills ids for methods annotated with {@link Seq}.
 *
 * @author livk
 * @see Seq
 * @see SequenceProvider
 */
@RequiredArgsConstructor
public class SequenceInterceptor extends AbstractAnnotationPointcutStrategyAdvisor<Seq> {

	private final ObjectProvider<SequenceProvider> providers;

	private final ExpressionResolver resolver = new SpringExpressionResolver();

	private volatile SequenceProvider provider;

	@Override
	protected Object doInvoke(MethodInvocation invocation, Seq seq) throws Throwable {
		String bizName = this.resolver.resolve(seq.value())
			.method(invocation.getMethod(), invocation.getArguments())
			.evaluate();
		if (!StringUtils.hasText(bizName)) {
			throw new SequenceException("Sequence bizName must not be empty");
		}
		Sequence sequence = getProvider().get(bizName);
		if (StringUtils.hasText(seq.param())) {
			fillParameterByName(invocation, seq.param(), sequence);
			return invocation.proceed();
		}
		if (seq.paramIndex() >= 0) {
			fillParameterByIndex(invocation, seq.paramIndex(), sequence);
			return invocation.proceed();
		}
		if (!isIdType(invocation.getMethod().getReturnType())) {
			throw new SequenceException("Seq without param requires a long or Long return type");
		}
		Object result = invocation.proceed();
		return isAbsent(result) ? sequence.nextValue() : result;
	}

	private void fillParameterByName(MethodInvocation invocation, String name, Sequence sequence) {
		Object[] arguments = invocation.getArguments();
		Parameter[] parameters = invocation.getMethod().getParameters();
		int index = -1;
		for (int i = 0; i < parameters.length; i++) {
			if (name.equals(parameters[i].getName())) {
				index = i;
				break;
			}
		}
		if (index < 0) {
			throw new SequenceException("Seq param '" + name + "' was not found");
		}
		fillArgument(arguments, index, parameters[index].getType(), "param '" + name + "'", sequence);
	}

	private void fillParameterByIndex(MethodInvocation invocation, int index, Sequence sequence) {
		Object[] arguments = invocation.getArguments();
		Parameter[] parameters = invocation.getMethod().getParameters();
		if (index >= parameters.length) {
			throw new SequenceException("Seq paramIndex '" + index + "' is out of range");
		}
		fillArgument(arguments, index, parameters[index].getType(), "paramIndex '" + index + "'", sequence);
	}

	private void fillArgument(Object[] arguments, int index, Class<?> type, String label, Sequence sequence) {
		if (!isIdType(type)) {
			throw new SequenceException("Seq " + label + " must be long or Long");
		}
		if (isAbsent(arguments[index])) {
			arguments[index] = sequence.nextValue();
		}
	}

	private SequenceProvider getProvider() {
		SequenceProvider result = this.provider;
		if (result == null) {
			synchronized (this) {
				result = this.provider;
				if (result == null) {
					result = this.providers.orderedStream()
						.findFirst()
						.orElseThrow(() -> new NoSuchBeanDefinitionException(SequenceProvider.class));
					this.provider = result;
				}
			}
		}
		return result;
	}

	private static boolean isIdType(Class<?> type) {
		return type == long.class || type == Long.class;
	}

	private static boolean isAbsent(Object value) {
		return value == null || (value instanceof Number number && number.longValue() <= 0);
	}

}
