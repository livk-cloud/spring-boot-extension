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
