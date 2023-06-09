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

package com.livk.autoconfigure.useragent.yauaa;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.useragent.UserAgentAutoConfiguration;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * The type Yauaa auto configuration.
 *
 * @author livk
 */
@SpringAutoService
@AutoConfiguration
@ConditionalOnClass(UserAgentAnalyzer.class)
@ImportAutoConfiguration(UserAgentAutoConfiguration.class)
public class YauaaAutoConfiguration {

	/**
	 * User agent analyzer user agent analyzer.
	 *
	 * @return the user agent analyzer
	 */
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public UserAgentAnalyzer userAgentAnalyzer() {
		return UserAgentAnalyzer
			.newBuilder()
			.hideMatcherLoadStats()
			.withCache(10000)
			.build();
	}

	/**
	 * Yauaa user agent parse yauaa user agent parse.
	 *
	 * @param userAgentAnalyzer the user agent analyzer
	 * @return the yauaa user agent parse
	 */
	@Bean
	@ConditionalOnMissingBean
	public YauaaUserAgentConverter yauaaUserAgentConverter(UserAgentAnalyzer userAgentAnalyzer) {
		return new YauaaUserAgentConverter(userAgentAnalyzer);
	}
}
