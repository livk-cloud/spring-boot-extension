/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.commons.web;

import com.livk.commons.jackson.JsonMapperUtils;
import com.livk.commons.util.HttpServletUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class ResponseWrapperTests {

	@Test
	void replaceBody() throws IOException {
		MockHttpServletResponse response = new MockHttpServletResponse();

		Map<String, String> result = Map.of("username", "livk", "password", "123456");
		ResponseWrapper wrapper = new ResponseWrapper(response);
		HttpServletUtils.outJson(wrapper, result);

		assertThat(wrapper.getContentAsByteArray()).isEqualTo(JsonMapperUtils.writeValueAsBytes(result));
		assertThat(wrapper.getContentAsString()).isEqualTo(JsonMapperUtils.writeValueAsString(result));

		wrapper.replaceBody(JsonMapperUtils.writeValueAsBytes(Map.of("root", "root")));

		assertThat(wrapper.getContentAsByteArray())
			.isEqualTo(JsonMapperUtils.writeValueAsBytes(Map.of("root", "root")));
		assertThat(wrapper.getContentAsString()).isEqualTo(JsonMapperUtils.writeValueAsString(Map.of("root", "root")));
	}

}
