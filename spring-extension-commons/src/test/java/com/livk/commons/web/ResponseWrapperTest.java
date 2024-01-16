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
 * <p>
 * ResponseWrapperTest
 * </p>
 *
 * @author livk
 * @date 2024/1/16
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
