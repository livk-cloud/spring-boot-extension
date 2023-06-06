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

package com.livk.autoconfigure.useragent.browscap;

import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;
import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.useragent.UserAgentAutoConfiguration;
import com.livk.autoconfigure.useragent.browscap.support.BrowscapUserAgentParse;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Arrays;

/**
 * The type Browscap auto configuration.
 *
 * @author livk
 */
@SpringAutoService
@AutoConfiguration
@ConditionalOnClass(UserAgentParser.class)
@ImportAutoConfiguration(UserAgentAutoConfiguration.class)
public class BrowscapAutoConfiguration {

	/**
	 * User agent parser user agent parser.
	 *
	 * @return the user agent parser
	 * @throws IOException    the io exception
	 * @throws ParseException the parse exception
	 */
	@Bean
	public UserAgentParser userAgentParser() throws IOException, ParseException {
		return new UserAgentService().loadParser(Arrays.asList(BrowsCapField.values()));
	}

	/**
	 * Browscap user agent parse browscap user agent parse.
	 *
	 * @param userAgentAnalyzer the user agent analyzer
	 * @return the browscap user agent parse
	 */
	@Bean
	@ConditionalOnMissingBean
	public BrowscapUserAgentParse browscapUserAgentParse(UserAgentParser userAgentAnalyzer) {
		return new BrowscapUserAgentParse(userAgentAnalyzer);
	}
}
