package com.livk.core.useragent;

import com.livk.core.useragent.domain.UserAgent;
import com.livk.core.useragent.support.UserAgentConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * UserAgentConverterTest
 * </p>
 *
 * @author livk
 */

@ContextConfiguration(classes = { BrowscapConfig.class, YauaaConfig.class })
@ExtendWith(SpringExtension.class)
class UserAgentConverterTest {

	@Autowired
	@Qualifier("browscapUserAgentConverter")
	UserAgentConverter browscapUserAgentConverter;

	@Autowired
	@Qualifier("yauaaUserAgentConverter")
	UserAgentConverter yauaaUserAgentConverter;

	HttpHeaders headers = new HttpHeaders();

	String userAgentStr = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

	@BeforeEach
	public void init() {
		headers.add(HttpHeaders.USER_AGENT, userAgentStr);
	}

	@Test
	void testBrowscap() {
		UserAgent userAgent = browscapUserAgentConverter.convert(headers);
		assertNotNull(userAgent);
		assertEquals(userAgentStr, userAgent.userAgentStr());
		assertEquals("Chrome", userAgent.browser());
		assertEquals("Browser", userAgent.browserType());
		assertEquals("120", userAgent.browserVersion());
		assertEquals("Win10", userAgent.os());
		assertEquals("10.0", userAgent.osVersion());
		assertEquals("Desktop", userAgent.deviceType());
		assertEquals("Windows Desktop", userAgent.deviceName());
		assertNull(userAgent.deviceBrand());
	}

	@Test
	void testYauaa() {
		UserAgent userAgent = yauaaUserAgentConverter.convert(headers);
		assertNotNull(userAgent);
		assertEquals(userAgentStr, userAgent.userAgentStr());
		assertEquals("Chrome", userAgent.browser());
		assertEquals("Browser", userAgent.browserType());
		assertEquals("120", userAgent.browserVersion());
		assertEquals("Windows NT", userAgent.os());
		assertEquals("Windows NT ??", userAgent.osVersion());
		assertEquals("Desktop", userAgent.deviceType());
		assertEquals("Desktop", userAgent.deviceName());
		assertNull(userAgent.deviceBrand());
	}

}
