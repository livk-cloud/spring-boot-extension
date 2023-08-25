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

package com.livk.commons.http.support;

import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.http.client.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * The type Ok http client http request.
 *
 * @author livk
 */
@RequiredArgsConstructor
class OkHttpClientHttpRequest extends AbstractClientHttpRequest
	implements StreamingHttpOutputMessage {

	private final OkHttpClient client;

	private final URI uri;

	private final HttpMethod method;

	private Body body;

	private FastByteArrayOutputStream bodyStream;


	@NonNull
	@Override
	public HttpMethod getMethod() {
		return this.method;
	}

	@NonNull
	@Override
	public URI getURI() {
		return this.uri;
	}

	@Override
	public void setBody(@NonNull Body body) {
		assertNotExecuted();
		this.body = body;
	}

	@NonNull
	@Override
	protected OutputStream getBodyInternal(@NonNull HttpHeaders headers) {
		if (this.bodyStream == null) {
			this.bodyStream = new FastByteArrayOutputStream(1024);
		}
		return this.bodyStream;
	}

	@NonNull
	@Override
	protected ClientHttpResponse executeInternal(@NonNull HttpHeaders headers) throws IOException {
		if (this.body == null && this.bodyStream != null) {
			this.body = outputStream -> this.bodyStream.writeTo(outputStream);
		}
		RequestBody requestBody;
		if (body != null) {
			requestBody = new BodyRequestBody(headers, body);
		} else if (okhttp3.internal.http.HttpMethod.requiresRequestBody(getMethod().name())) {
			String header = headers.getFirst(HttpHeaders.CONTENT_TYPE);
			MediaType contentType = (header != null) ? MediaType.parse(header) : null;
			requestBody = RequestBody.create(new byte[0], contentType);
		} else {
			requestBody = null;
		}
		Request.Builder builder = new Request.Builder()
			.url(this.uri.toURL());
		builder.method(this.method.name(), requestBody);
		headers.forEach((headerName, headerValues) -> {
			for (String headerValue : headerValues) {
				builder.addHeader(headerName, headerValue);
			}
		});
		Request request = builder.build();
		return new OkHttpClientHttpResponse(this.client.newCall(request).execute());
	}

	@RequiredArgsConstructor

	private static class BodyRequestBody extends RequestBody {

		private final HttpHeaders headers;

		private final Body body;


		@Override
		public long contentLength() {
			return this.headers.getContentLength();
		}

		@Nullable
		@Override
		public MediaType contentType() {
			String contentType = this.headers.getFirst(HttpHeaders.CONTENT_TYPE);
			if (StringUtils.hasText(contentType)) {
				return MediaType.parse(contentType);
			} else {
				return null;
			}
		}

		@Override
		public void writeTo(BufferedSink sink) throws IOException {
			this.body.writeTo(sink.outputStream());
		}

		@Override
		public boolean isOneShot() {
			return true;
		}
	}


}
