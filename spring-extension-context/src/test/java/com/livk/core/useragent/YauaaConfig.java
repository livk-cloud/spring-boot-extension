package com.livk.core.useragent;

import com.livk.core.useragent.support.UserAgentConverter;
import com.livk.core.useragent.yauaa.YauaaUserAgentConverter;
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
