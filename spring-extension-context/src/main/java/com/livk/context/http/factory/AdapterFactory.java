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

package com.livk.context.http.factory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.Ordered;
import org.springframework.web.service.invoker.HttpExchangeAdapter;

/**
 * @author livk
 */
public interface AdapterFactory<H extends HttpExchangeAdapter> extends Ordered {

	boolean support();

	H create(BeanFactory beanFactory);

	AdapterType type();

	@Override
	default int getOrder() {
		return 0;
	}

}
