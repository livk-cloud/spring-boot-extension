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

package com.livk.core.useragent;

import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;
import com.livk.core.useragent.browscap.BrowscapUserAgentConverter;
import com.livk.core.useragent.support.UserAgentConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;

/**
 * <p>
 * BrowscapConfig
 * </p>
 *
 * @author livk
 */
@Configuration
class BrowscapConfig {

	@Bean
	public UserAgentConverter browscapUserAgentConverter() throws IOException, ParseException {
		UserAgentParser userAgentParser = new UserAgentService().loadParser(Arrays.asList(BrowsCapField.values()));
		return new BrowscapUserAgentConverter(userAgentParser);
	}

}
