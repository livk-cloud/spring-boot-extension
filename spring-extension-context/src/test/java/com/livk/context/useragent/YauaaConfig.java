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

package com.livk.context.useragent;

import com.livk.context.useragent.support.UserAgentConverter;
import com.livk.context.useragent.yauaa.YauaaUserAgentConverter;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * YauaaConfig
 * </p>
 *
 * @author livk
 */
@Configuration
class YauaaConfig {

	@Bean
	public UserAgentConverter yauaaUserAgentConverter() {
		UserAgentAnalyzer analyzer = UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(10).build();
		return new YauaaUserAgentConverter(analyzer);
	}

}
