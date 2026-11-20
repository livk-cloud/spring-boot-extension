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

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * @author livk
 */
class OkHttpClientHttpRequestFactoryTests {

	MockWebServer server;

	@BeforeEach
	void setUp() throws IOException {
		this.server = new MockWebServer();
		this.server.start();
	}

	@AfterEach
	void tearDown() throws IOException {
		this.server.shutdown();
	}

	@Test
	void createRequestAndExecuteGet() throws Exception {
		this.server.enqueue(new MockResponse().setResponseCode(200)
			.setHeader("Content-Type", "text/plain")
			.setHeader("X-Test", "ok")
			.setBody("hello"));

		OkHttpClientHttpRequestFactory factory = new OkHttpClientHttpRequestFactory();
		URI uri = this.server.url("/hello").uri();
		ClientHttpRequest request = factory.createRequest(uri, HttpMethod.GET);
		request.getHeaders().add("X-Req", "1");

		try (ClientHttpResponse response = request.execute()) {
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
			assertThat(response.getStatusText()).isNotNull();
			assertThat(response.getHeaders().getFirst("X-Test")).isEqualTo("ok");
			assertThat(StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8)).isEqualTo("hello");
		}

		RecordedRequest recorded = this.server.takeRequest();
		assertThat(recorded.getMethod()).isEqualTo("GET");
		assertThat(recorded.getPath()).isEqualTo("/hello");
		assertThat(recorded.getHeader("X-Req")).isEqualTo("1");
		factory.destroy();
	}

	@Test
	void executePostWithBodyStreamAndContentType() throws Exception {
		this.server.enqueue(new MockResponse().setResponseCode(201).setBody("created"));

		OkHttpClientHttpRequestFactory factory = new OkHttpClientHttpRequestFactory(new OkHttpClient());
		ClientHttpRequest request = factory.createRequest(this.server.url("/post").uri(), HttpMethod.POST);
		request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		request.getBody().write("{\"a\":1}".getBytes(StandardCharsets.UTF_8));

		try (ClientHttpResponse response = request.execute()) {
			assertThat(response.getStatusCode().value()).isEqualTo(201);
			assertThat(StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8)).isEqualTo("created");
		}

		RecordedRequest recorded = this.server.takeRequest();
		assertThat(recorded.getMethod()).isEqualTo("POST");
		assertThat(recorded.getHeader("Content-Type")).contains("application/json");
		assertThat(recorded.getBody().readUtf8()).isEqualTo("{\"a\":1}");
		factory.destroy();
	}

	@Test
	void executePostWithStreamingBody() throws Exception {
		this.server.enqueue(new MockResponse().setResponseCode(200).setBody("ok"));

		OkHttpClientHttpRequestFactory factory = new OkHttpClientHttpRequestFactory();
		OkHttpClientHttpRequest request = (OkHttpClientHttpRequest) factory
			.createRequest(this.server.url("/stream").uri(), HttpMethod.POST);
		request.getHeaders().setContentType(MediaType.TEXT_PLAIN);
		request.getHeaders().setContentLength(5);
		request.setBody((outputStream) -> outputStream.write("hello".getBytes(StandardCharsets.UTF_8)));

		try (ClientHttpResponse response = request.execute()) {
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		}

		RecordedRequest recorded = this.server.takeRequest();
		assertThat(recorded.getBody().readUtf8()).isEqualTo("hello");
		factory.destroy();
	}

	@Test
	void executePostWithoutBodyUsesEmptyRequestBody() throws Exception {
		this.server.enqueue(new MockResponse().setResponseCode(204));

		OkHttpClientHttpRequestFactory factory = new OkHttpClientHttpRequestFactory();
		ClientHttpRequest request = factory.createRequest(this.server.url("/empty").uri(), HttpMethod.POST);

		try (ClientHttpResponse response = request.execute()) {
			assertThat(response.getStatusCode().value()).isEqualTo(204);
		}

		RecordedRequest recorded = this.server.takeRequest();
		assertThat(recorded.getMethod()).isEqualTo("POST");
		assertThat(recorded.getBodySize()).isZero();
		factory.destroy();
	}

	@Test
	void timeoutAndConnectionPoolCustomizers() {
		OkHttpClientHttpRequestFactory factory = new OkHttpClientHttpRequestFactory().readTimeout(1, TimeUnit.SECONDS)
			.writeTimeout(2, TimeUnit.SECONDS)
			.connectTimeout(3, TimeUnit.SECONDS)
			.readTimeout(Duration.ofMillis(100))
			.writeTimeout(Duration.ofMillis(200))
			.connectTimeout(Duration.ofMillis(300))
			.connectionPool(new ConnectionPool(5, 1, TimeUnit.MINUTES));

		assertThat(factory.createRequest(URI.create("http://localhost"), HttpMethod.GET)).isNotNull();
	}

	@Test
	void constructorRejectsNullClient() {
		assertThatIllegalArgumentException().isThrownBy(() -> new OkHttpClientHttpRequestFactory(null))
			.withMessageContaining("OkHttpClient must not be null");
	}

	@Test
	void destroySkipsCleanupForExternalClient() throws Exception {
		OkHttpClient client = new OkHttpClient();
		OkHttpClientHttpRequestFactory factory = new OkHttpClientHttpRequestFactory(client);
		factory.destroy();
		assertThat(client.connectionPool()).isNotNull();
	}

}
