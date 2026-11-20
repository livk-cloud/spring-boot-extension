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

package com.livk.context.useragent;

import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;
import com.livk.context.useragent.browscap.BrowscapUserAgentConverter;
import com.livk.context.useragent.yauaa.YauaaUserAgentConverter;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author livk
 */
class UserAgentCompositeTests {

	ApplicationContext applicationContext;

	final HttpHeaders headers = new HttpHeaders();

	final String userAgentStr = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

	@BeforeEach
	public void init() {
		headers.add(HttpHeaders.USER_AGENT, userAgentStr);
	}

	@Test
	void convertBrowscap() {
		applicationContext = new AnnotationConfigApplicationContext(BrowscapConfig.class);

		UserAgentComposite userAgentComposite = new UserAgentComposite(
				applicationContext.getBeanProvider(UserAgentConverter.class));
		UserAgent userAgent = userAgentComposite.convert(headers);

		assertThat(userAgent).isNotNull();
		assertThat(userAgent.userAgentStr()).isEqualTo(userAgentStr);
		assertThat(userAgent.browser()).isEqualTo("Chrome");
		assertThat(userAgent.browserType()).isEqualTo("Browser");
		assertThat(userAgent.browserVersion()).isEqualTo("120");
		assertThat(userAgent.os()).isEqualTo("Win10");
		assertThat(userAgent.osVersion()).isEqualTo("10.0");
		assertThat(userAgent.deviceType()).isEqualTo("Desktop");
		assertThat(userAgent.deviceName()).isEqualTo("Windows Desktop");
		assertThat(userAgent.deviceBrand()).isEqualTo("Unknown");
	}

	@Test
	void convertYauaa() {
		applicationContext = new AnnotationConfigApplicationContext(YauaaConfig.class);

		UserAgentComposite userAgentComposite = new UserAgentComposite(
				applicationContext.getBeanProvider(UserAgentConverter.class));
		UserAgent userAgent = userAgentComposite.convert(headers);

		assertThat(userAgent).isNotNull();
		assertThat(userAgent.userAgentStr()).isEqualTo(userAgentStr);
		assertThat(userAgent.browser()).isEqualTo("Chrome");
		assertThat(userAgent.browserType()).isEqualTo("Browser");
		assertThat(userAgent.browserVersion()).isEqualTo("120");
		assertThat(userAgent.os()).isEqualTo("Windows NT");
		assertThat(userAgent.osVersion()).isEqualTo("Windows NT ??");
		assertThat(userAgent.deviceType()).isEqualTo("Desktop");
		assertThat(userAgent.deviceName()).isEqualTo("Desktop");
		assertThat(userAgent.deviceBrand()).isEqualTo("Unknown");
	}

	@Test
	@SuppressWarnings("unchecked")
	void convertSkipsNullAndUsesNextConverter() {
		UserAgentConverter first = mock(UserAgentConverter.class);
		UserAgentConverter second = mock(UserAgentConverter.class);
		UserAgent expected = mock(UserAgent.class);
		given(first.convert(headers)).willReturn(null);
		given(second.convert(headers)).willReturn(expected);

		ObjectProvider<UserAgentConverter> provider = mock(ObjectProvider.class);
		given(provider.orderedStream()).willReturn(Stream.of(first, second));

		assertThat(new UserAgentComposite(provider).convert(headers)).isSameAs(expected);
	}

	@Test
	@SuppressWarnings("unchecked")
	void convertThrowsWhenEveryConverterReturnsNull() {
		UserAgentConverter converter = mock(UserAgentConverter.class);
		given(converter.convert(headers)).willReturn(null);
		ObjectProvider<UserAgentConverter> provider = mock(ObjectProvider.class);
		given(provider.orderedStream()).willReturn(Stream.of(converter));

		assertThatThrownBy(() -> new UserAgentComposite(provider).convert(headers))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("User-Agent");
	}

	@Test
	@SuppressWarnings("unchecked")
	void convertCachesConverterList() {
		UserAgentConverter converter = mock(UserAgentConverter.class);
		UserAgent expected = mock(UserAgent.class);
		given(converter.convert(headers)).willReturn(expected);
		ObjectProvider<UserAgentConverter> provider = mock(ObjectProvider.class);
		given(provider.orderedStream()).willReturn(Stream.of(converter));

		UserAgentComposite composite = new UserAgentComposite(provider);
		assertThat(composite.convert(headers)).isSameAs(expected);
		assertThat(composite.convert(headers)).isSameAs(expected);
		verify(provider, times(1)).orderedStream();
	}

	@TestConfiguration
	static class BrowscapConfig {

		@Bean
		public UserAgentConverter browscapUserAgentConverter() throws IOException, ParseException {
			UserAgentParser userAgentParser = new UserAgentService().loadParser(Arrays.asList(BrowsCapField.values()));
			return new BrowscapUserAgentConverter(userAgentParser);
		}

	}

	@TestConfiguration
	static class YauaaConfig {

		@Bean
		public UserAgentConverter yauaaUserAgentConverter() {
			UserAgentAnalyzer analyzer = UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(10).build();
			return new YauaaUserAgentConverter(analyzer);
		}

	}

}
