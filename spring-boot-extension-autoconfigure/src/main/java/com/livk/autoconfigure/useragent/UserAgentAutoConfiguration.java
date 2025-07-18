/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
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

package com.livk.autoconfigure.useragent;

import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;
import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.context.useragent.browscap.BrowscapUserAgentConverter;
import com.livk.context.useragent.yauaa.YauaaUserAgentConverter;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.Arrays;

/**
 * The type User agent auto configuration.
 */
@SpringAutoService
@AutoConfiguration(before = UserAgentConfiguration.class)
@ImportAutoConfiguration(UserAgentConfiguration.class)
public class UserAgentAutoConfiguration {

	/**
	 * The type Browscap auto configuration.
	 */
	@AutoConfiguration
	@ConditionalOnClass(UserAgentParser.class)
	public static class BrowscapAutoConfiguration {

		/**
		 * User agent parser user agent parser.
		 * @return the user agent parser
		 * @throws IOException the io exception
		 * @throws ParseException the parse exception
		 */
		@Bean
		public UserAgentParser userAgentParser() throws IOException, ParseException {
			return new UserAgentService().loadParser(Arrays.asList(BrowsCapField.values()));
		}

		/**
		 * Browscap user agent parse browscap user agent parse.
		 * @param userAgentAnalyzer the user agent analyzer
		 * @return the browscap user agent parse
		 */
		@Bean
		@ConditionalOnMissingBean
		public BrowscapUserAgentConverter browscapUserAgentConverter(UserAgentParser userAgentAnalyzer) {
			return new BrowscapUserAgentConverter(userAgentAnalyzer);
		}

	}

	/**
	 * The type Yauaa auto configuration.
	 */
	@AutoConfiguration
	@ConditionalOnClass(UserAgentAnalyzer.class)
	public static class YauaaAutoConfiguration {

		/**
		 * User agent analyzer user agent analyzer.
		 * @return the user agent analyzer
		 */
		@Bean
		@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
		public UserAgentAnalyzer userAgentAnalyzer() {
			return UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(10000).build();
		}

		/**
		 * Yauaa user agent parse yauaa user agent parse.
		 * @param userAgentAnalyzer the user agent analyzer
		 * @return the yauaa user agent parse
		 */
		@Bean
		@ConditionalOnMissingBean
		public YauaaUserAgentConverter yauaaUserAgentConverter(UserAgentAnalyzer userAgentAnalyzer) {
			return new YauaaUserAgentConverter(userAgentAnalyzer);
		}

	}

}
