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

package com.livk.commons.web;

import com.livk.commons.jackson.util.JsonMapperUtils;
import com.livk.commons.util.WebUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class ResponseWrapperTest {

	ResponseWrapper wrapper;

	@Test
	void getResponseData() throws IOException {
		MockHttpServletResponse response = new MockHttpServletResponse();
		Map<String, String> result = Map.of("username", "livk", "password", "123456");
		wrapper = new ResponseWrapper(response);
		WebUtils.outJson(wrapper, result);

		assertArrayEquals(JsonMapperUtils.writeValueAsBytes(result), wrapper.getResponseData());
		assertEquals(JsonMapperUtils.writeValueAsString(result), wrapper.getOutputStream().toString());

		wrapper.setResponseData(JsonMapperUtils.writeValueAsBytes(Map.of("root", "root")));

		assertArrayEquals(JsonMapperUtils.writeValueAsBytes(Map.of("root", "root")), wrapper.getResponseData());
		assertEquals(JsonMapperUtils.writeValueAsString(Map.of("root", "root")), wrapper.getOutputStream().toString());

	}

}
