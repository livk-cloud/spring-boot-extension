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

package com.livk.commons.web;

import lombok.experimental.Delegate;
import org.jspecify.annotations.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 根据{@link org.springframework.http.HttpHeaders}改造
 * <p>
 * 用于HttpParam，适用于Query和FORM请求数据存储
 * </p>
 *
 * @author livk
 * @since 1.3.5
 * @see org.springframework.http.HttpHeaders
 */
public final class HttpParameters implements MultiValueMap<String, String>, Serializable {

	@Serial
	private static final long serialVersionUID = -578554704772377436L;

	@Delegate
	final MultiValueMap<String, String> parameters;

	public HttpParameters() {
		this(CollectionUtils.toMultiValueMap(new LinkedMultiValueMap<>(8)));
	}

	public HttpParameters(MultiValueMap<String, String> parameters) {
		Assert.notNull(parameters, "MultiValueMap must not be null");
		this.parameters = parameters;
	}

	/**
	 * @deprecated since 1.5.0 use {@link #HttpParameters(MultiValueMap)}
	 * @see #HttpParameters(MultiValueMap)
	 * @see CollectionUtils#toMultiValueMap
	 * @see MultiValueMap#fromMultiValue(Map)
	 */
	@Deprecated(since = "1.5.0")
	public HttpParameters(Map<String, List<String>> map) {
		this(CollectionUtils.toMultiValueMap(map));
	}

	public List<String> getOrEmpty(Object parameterName) {
		List<String> values = get(parameterName);
		return (values != null ? values : Collections.emptyList());
	}

	@Override
	public boolean equals(@Nullable Object other) {
		return (this == other || (other instanceof HttpParameters that && unwrap(this).equals(unwrap(that))));
	}

	@Override
	public int hashCode() {
		return this.parameters.hashCode();
	}

	@Override
	public String toString() {
		return formatParameters(this.parameters);
	}

	private static MultiValueMap<String, String> unwrap(HttpParameters parameters) {
		while (parameters.parameters instanceof HttpParameters httpParameters) {
			parameters = httpParameters;
		}
		return parameters.parameters;
	}

	public static String formatParameters(MultiValueMap<String, String> parameters) {
		return parameters.entrySet().stream().map(entry -> {
			List<String> values = entry.getValue();
			return entry.getKey() + ":" + (values.size() == 1 ? "\"" + values.getFirst() + "\""
					: values.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")));
		}).collect(Collectors.joining(", ", "[", "]"));
	}

}
