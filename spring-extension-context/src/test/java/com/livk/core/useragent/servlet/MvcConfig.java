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

package com.livk.core.useragent.servlet;

import com.livk.core.useragent.UserAgentHelper;
import com.livk.core.useragent.support.UserAgentConverter;
import com.livk.core.useragent.yauaa.YauaaUserAgentConverter;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;

/**
 * @author livk
 */
@Configuration
class MvcConfig {

	@Bean
	public UserAgentConverter yauaaUserAgentConverter() {
		UserAgentAnalyzer analyzer = UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(10).build();
		return new YauaaUserAgentConverter(analyzer);
	}

	@Bean
	public ConversionService conversionService(ObjectProvider<Converter<?, ?>> converters) {
		GenericConversionService conversionService = new GenericConversionService();
		converters.orderedStream().forEach(conversionService::addConverter);
		return conversionService;
	}

	@Bean
	public UserAgentHelper userAgentHelper(ApplicationContext applicationContext) {
		return new UserAgentHelper(applicationContext);
	}

}
