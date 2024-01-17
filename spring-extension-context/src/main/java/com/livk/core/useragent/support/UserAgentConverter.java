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

package com.livk.core.useragent.support;

import com.livk.core.useragent.domain.UserAgent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;

/**
 * @author livk
 */
public interface UserAgentConverter extends Converter<HttpHeaders, UserAgent> {

	@NonNull
	@Override
	default <U> Converter<HttpHeaders, U> andThen(@NonNull Converter<? super UserAgent, ? extends U> after) {
		throw new UnsupportedOperationException();
	}

}
