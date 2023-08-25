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

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * The type Ok http client http request factory.
 *
 * @author livk
 */
public class OkHttpClientHttpRequestFactory implements ClientHttpRequestFactory, DisposableBean {

	private OkHttpClient client;

	private final boolean defaultClient;


	/**
	 * Create a factory with a default {@link OkHttpClient} instance.
	 */
	public OkHttpClientHttpRequestFactory() {
		this.client = new OkHttpClient();
		this.defaultClient = true;
	}

	/**
	 * Create a factory with the given {@link OkHttpClient} instance.
	 *
	 * @param client the client to use
	 */
	public OkHttpClientHttpRequestFactory(OkHttpClient client) {
		Assert.notNull(client, "OkHttpClient must not be null");
		this.client = client;
		this.defaultClient = false;
	}


	/**
	 * Set the underlying read timeout in milliseconds.
	 * A value of 0 specifies an infinite timeout.
	 *
	 * @param readTimeout the read timeout
	 * @param unit        the unit
	 * @return the ok http client http request factory
	 */
	public OkHttpClientHttpRequestFactory readTimeout(long readTimeout, TimeUnit unit) {
		this.client = this.client.newBuilder()
			.readTimeout(readTimeout, unit)
			.build();
		return this;
	}

	/**
	 * Set the underlying read timeout in milliseconds.
	 * A value of 0 specifies an infinite timeout.
	 *
	 * @param readTimeout the read timeout
	 * @return the ok http client http request factory
	 * @since 6.1
	 */
	public OkHttpClientHttpRequestFactory readTimeout(Duration readTimeout) {
		this.client = this.client.newBuilder()
			.readTimeout(readTimeout)
			.build();
		return this;
	}

	/**
	 * Set the underlying write timeout in milliseconds.
	 * A value of 0 specifies an infinite timeout.
	 *
	 * @param writeTimeout the write timeout
	 * @param unit         the unit
	 * @return the ok http client http request factory
	 */
	public OkHttpClientHttpRequestFactory writeTimeout(long writeTimeout, TimeUnit unit) {
		this.client = this.client.newBuilder()
			.writeTimeout(writeTimeout, unit)
			.build();
		return this;
	}

	/**
	 * Set the underlying write timeout in milliseconds.
	 * A value of 0 specifies an infinite timeout.
	 *
	 * @param writeTimeout the write timeout
	 * @return the ok http client http request factory
	 * @since 6.1
	 */
	public OkHttpClientHttpRequestFactory writeTimeout(Duration writeTimeout) {
		this.client = this.client.newBuilder()
			.writeTimeout(writeTimeout)
			.build();
		return this;
	}

	/**
	 * Set the underlying connect timeout in milliseconds.
	 * A value of 0 specifies an infinite timeout.
	 *
	 * @param connectTimeout the connect timeout
	 * @param unit           the unit
	 * @return the ok http client http request factory
	 */
	public OkHttpClientHttpRequestFactory connectTimeout(long connectTimeout, TimeUnit unit) {
		this.client = this.client.newBuilder()
			.connectTimeout(connectTimeout, unit)
			.build();
		return this;
	}

	/**
	 * Set the underlying connect timeout in milliseconds.
	 * A value of 0 specifies an infinite timeout.
	 *
	 * @param connectTimeout the connect timeout
	 * @return the ok http client http request factory
	 * @since 6.1
	 */
	public OkHttpClientHttpRequestFactory connectTimeout(Duration connectTimeout) {
		this.client = this.client.newBuilder()
			.connectTimeout(connectTimeout)
			.build();
		return this;
	}

	/**
	 * Connection pool ok http client http request factory.
	 *
	 * @param connectionPool the connection pool
	 * @return the ok http client http request factory
	 */
	public OkHttpClientHttpRequestFactory connectionPool(ConnectionPool connectionPool) {
		OkHttpClient.Builder builder = this.client.newBuilder();
		builder.setConnectionPool$okhttp(connectionPool);
		this.client = builder.build();
		return this;
	}

	@NonNull
	@Override
	public ClientHttpRequest createRequest(@NonNull URI uri, @NonNull HttpMethod httpMethod) {
		return new OkHttpClientHttpRequest(this.client, uri, httpMethod);
	}


	@Override
	public void destroy() throws IOException {
		if (this.defaultClient) {
			// Clean up the client if we created it in the constructor
			Cache cache = this.client.cache();
			if (cache != null) {
				cache.close();
			}
			this.client.dispatcher().executorService().shutdown();
			this.client.connectionPool().evictAll();
		}
	}

}
