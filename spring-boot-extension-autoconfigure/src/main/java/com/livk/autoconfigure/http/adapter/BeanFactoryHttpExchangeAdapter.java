/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.autoconfigure.http.adapter;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.service.invoker.HttpExchangeAdapter;
import org.springframework.web.service.invoker.HttpRequestValues;

/**
 * The type Bean factory http exchange adapter.
 *
 * @author livk
 */
public class BeanFactoryHttpExchangeAdapter implements HttpExchangeAdapter {

	private final HttpExchangeAdapter adapter;

	/**
	 * Instantiates a new Bean factory http exchange adapter.
	 * @param factory the factory
	 * @param beanFactory the bean factory
	 */
	public BeanFactoryHttpExchangeAdapter(AdapterFactory<?> factory, BeanFactory beanFactory) {
		adapter = factory.create(beanFactory);
	}

	@Override
	public boolean supportsRequestAttributes() {
		return adapter.supportsRequestAttributes();
	}

	@Override
	public void exchange(@NonNull HttpRequestValues requestValues) {
		adapter.exchange(requestValues);
	}

	@NonNull
	@Override
	public HttpHeaders exchangeForHeaders(@NonNull HttpRequestValues requestValues) {
		return adapter.exchangeForHeaders(requestValues);
	}

	@Override
	public <T> T exchangeForBody(@NonNull HttpRequestValues requestValues,
			@NonNull ParameterizedTypeReference<T> bodyType) {
		return adapter.exchangeForBody(requestValues, bodyType);
	}

	@NonNull
	@Override
	public ResponseEntity<Void> exchangeForBodilessEntity(@NonNull HttpRequestValues requestValues) {
		return adapter.exchangeForBodilessEntity(requestValues);
	}

	@NonNull
	@Override
	public <T> ResponseEntity<T> exchangeForEntity(@NonNull HttpRequestValues requestValues,
			@NonNull ParameterizedTypeReference<T> bodyType) {
		return adapter.exchangeForEntity(requestValues, bodyType);
	}

}
