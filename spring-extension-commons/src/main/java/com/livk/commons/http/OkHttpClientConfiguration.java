/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.commons.http;

import com.livk.commons.http.support.OkHttpClientHttpRequestFactory;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Fallback;

/**
 * <p>
 * OkHttpClientConfiguration
 * </p>
 *
 * @author livk
 */
@AutoConfiguration
@ConditionalOnClass(OkHttpClient.class)
class OkHttpClientConfiguration {

	@Bean
	@Fallback
	@ConditionalOnMissingBean(OkHttpClient.class)
	public OkHttpClientHttpRequestFactory defaultOkHttpClientHttpRequestFactory() {
		ConnectionPool pool = new ConnectionPool();
		return new OkHttpClientHttpRequestFactory().connectionPool(pool);
	}

	/**
	 * Rest template customizer rest template customizer.
	 * @param okHttpClient the ok http client
	 * @return the rest template customizer
	 */
	@Bean
	@ConditionalOnBean(OkHttpClient.class)
	public OkHttpClientHttpRequestFactory okHttpClientHttpRequestFactory(OkHttpClient okHttpClient) {
		return new OkHttpClientHttpRequestFactory(okHttpClient);
	}

}
