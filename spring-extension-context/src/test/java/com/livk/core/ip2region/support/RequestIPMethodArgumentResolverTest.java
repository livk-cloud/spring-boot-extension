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

package com.livk.core.ip2region.support;

import com.livk.commons.io.ResourceUtils;
import com.livk.core.ip2region.Ip2RegionSearch;
import com.livk.core.ip2region.annotation.RequestIp;
import com.livk.core.ip2region.doamin.IpInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class RequestIPMethodArgumentResolverTest {

	static RequestIPMethodArgumentResolver resolver;

	static MethodParameter parameter;

	@BeforeAll
	static void init() throws Exception {
		Resource resource = ResourceUtils.getResource(ResourceUtils.CLASSPATH_URL_PREFIX + "ip/ip2region.xdb");
		Searcher searcher = Searcher.newWithBuffer(resource.getContentAsByteArray());
		Ip2RegionSearch search = new Ip2RegionSearch(searcher);
		resolver = new RequestIPMethodArgumentResolver(search);

		Method method = RequestIPMethodArgumentResolverTest.class.getDeclaredMethod("test", IpInfo.class);
		parameter = MethodParameter.forExecutable(method, 0);
	}

	@Test
	void supportsParameter() {
		assertTrue(resolver.supportsParameter(parameter));
	}

	@Test
	void resolveArgument() {
		assertNull(RequestIpContextHolder.get());

		ServletWebRequest webRequest = new ServletWebRequest(new MockHttpServletRequest());
		Object result = resolver.resolveArgument(parameter, null, webRequest, null);
		assertInstanceOf(IpInfo.class, result);

		IpInfo info = (IpInfo) result;
		assertNotNull(info.getIp());
		assertNull(info.getNation());
		assertNull(info.getArea());
		assertNull(info.getProvince());
		assertEquals("内网IP", info.getCity());
		assertEquals("内网IP", info.getOperator());

		assertEquals(result, RequestIpContextHolder.get());

		RequestIpContextHolder.remove();
	}

	@SuppressWarnings("unused")
	void test(@RequestIp IpInfo info) {
	}

}
