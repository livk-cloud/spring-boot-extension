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

package com.livk.context.dynamic.intercept;

import com.livk.commons.aop.AnnotationAbstractPointcutTypeAdvisor;
import com.livk.context.dynamic.DataSourceContextHolder;
import com.livk.context.dynamic.annotation.DynamicSource;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author livk
 */
public class DataSourceInterceptor extends AnnotationAbstractPointcutTypeAdvisor<DynamicSource> {

	@Override
	protected Object invoke(MethodInvocation invocation, DynamicSource dynamicSource) throws Throwable {
		if (dynamicSource != null) {
			DataSourceContextHolder.switchDataSource(dynamicSource.value());
		}
		Object proceed = invocation.proceed();
		DataSourceContextHolder.clear();
		return proceed;
	}

}
