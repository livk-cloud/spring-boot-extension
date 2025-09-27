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

package com.livk.commons.http.support;

import okhttp3.Response;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;

import java.io.InputStream;

/**
 * The type Ok http client http response.
 *
 * @author livk
 */
class OkHttpClientHttpResponse implements ClientHttpResponse {

	private final Response response;

	@Nullable private volatile HttpHeaders headers;

	/**
	 * Instantiates a new Ok http client http response.
	 * @param response the response
	 */
	OkHttpClientHttpResponse(Response response) {
		Assert.notNull(response, "Response must not be null");
		this.response = response;
	}

	@NonNull
	@Override
	public HttpStatusCode getStatusCode() {
		return HttpStatusCode.valueOf(this.response.code());
	}

	@NonNull
	@Override
	public String getStatusText() {
		return this.response.message();
	}

	@NonNull
	@Override
	public InputStream getBody() {
		return this.response.body().byteStream();
	}

	@NonNull
	@Override
	public HttpHeaders getHeaders() {
		HttpHeaders headers = this.headers;
		if (headers == null) {
			headers = new HttpHeaders();
			for (String headerName : this.response.headers().names()) {
				for (String headerValue : this.response.headers(headerName)) {
					headers.add(headerName, headerValue);
				}
			}
			this.headers = headers;
		}
		return headers;
	}

	@Override
	public void close() {
		this.response.body().close();
	}

}
