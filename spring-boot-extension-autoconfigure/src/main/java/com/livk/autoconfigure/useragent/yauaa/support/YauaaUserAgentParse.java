/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.autoconfigure.useragent.yauaa.support;

import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;
import com.livk.commons.bean.GenericWrapper;
import com.livk.commons.bean.Wrapper;
import lombok.RequiredArgsConstructor;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.http.HttpHeaders;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * YauaaUserAgentParse
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public class YauaaUserAgentParse implements HttpUserAgentParser {

	private final UserAgentAnalyzer userAgentAnalyzer;

	@Override
	public Wrapper parse(HttpHeaders headers) {
		Map<String, String> headersConcat = headers.entrySet()
			.stream()
			.collect(Collectors.toMap(Map.Entry::getKey,
				entry -> String.join(",", entry.getValue())));
		UserAgent userAgent = userAgentAnalyzer.parse(headersConcat);
		return GenericWrapper.of(userAgent);
	}
}
