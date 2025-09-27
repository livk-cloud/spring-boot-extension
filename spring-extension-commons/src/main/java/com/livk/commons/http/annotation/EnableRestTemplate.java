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

package com.livk.commons.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 用于生成配置文件
 * <p>
 * {@code @SpringAutoService(EnableRestTemplate.class)}
 * <p>
 * {@link HttpClientType#REST_TEMPLATE}
 *
 * @author livk
 * @deprecated use {@link EnableRestClient}
 * @see EnableHttpClient
 * @see com.livk.commons.http.RestTemplateConfiguration
 */
@Deprecated(since = "1.4.3")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableHttpClient(HttpClientType.REST_TEMPLATE)
public @interface EnableRestTemplate {

}
