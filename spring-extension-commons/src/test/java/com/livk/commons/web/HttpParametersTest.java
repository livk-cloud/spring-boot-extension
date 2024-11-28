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

package com.livk.commons.web;

import com.livk.commons.util.WebUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * HttpParametersTest
 * </p>
 *
 * @author livk
 */
class HttpParametersTest {

	@Test
	void test() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("username", "livk", "root", "admin");
		request.addParameter("password", "123456");
		MultiValueMap<String, String> params = WebUtils.params(request);
		HttpParameters parameters = new HttpParameters(params);
		assertEquals(List.of("livk", "root", "admin"), parameters.get("username"));
		assertEquals("livk", parameters.getFirst("username"));
		assertEquals("123456", parameters.getFirst("password"));
	}
}
