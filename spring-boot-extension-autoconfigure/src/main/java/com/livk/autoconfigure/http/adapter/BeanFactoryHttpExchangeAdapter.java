package com.livk.autoconfigure.http.adapter;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.service.invoker.HttpExchangeAdapter;
import org.springframework.web.service.invoker.HttpRequestValues;

/**
 * @author livk
 */
public class BeanFactoryHttpExchangeAdapter implements HttpExchangeAdapter {

	private final HttpExchangeAdapter adapter;

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
	public <T> T exchangeForBody(@NonNull HttpRequestValues requestValues, @NonNull ParameterizedTypeReference<T> bodyType) {
		return adapter.exchangeForBody(requestValues, bodyType);
	}

	@NonNull
	@Override
	public ResponseEntity<Void> exchangeForBodilessEntity(@NonNull HttpRequestValues requestValues) {
		return adapter.exchangeForBodilessEntity(requestValues);
	}

	@NonNull
	@Override
	public <T> ResponseEntity<T> exchangeForEntity(@NonNull HttpRequestValues requestValues, @NonNull ParameterizedTypeReference<T> bodyType) {
		return adapter.exchangeForEntity(requestValues, bodyType);
	}
}
