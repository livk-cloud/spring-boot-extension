/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.commons.http.annotation;

import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;

/**
 * http客户端类型
 *
 * @author livk
 */
@RequiredArgsConstructor
public enum HttpClientType {

	/**
	 * RestTemplate
	 */
	@Deprecated(since = "1.4.3")
	REST_TEMPLATE(EnableRestTemplate.class),
	/**
	 * WebClient
	 */
	WEB_CLIENT(EnableWebClient.class),

	/**
	 * Rest client http client type.
	 */
	REST_CLIENT(EnableRestClient.class);

	private final Class<? extends Annotation> annotationType;

	/**
	 * 返回相关注解
	 * @return the class
	 */
	public Class<? extends Annotation> annotationType() {
		return annotationType;
	}

}
