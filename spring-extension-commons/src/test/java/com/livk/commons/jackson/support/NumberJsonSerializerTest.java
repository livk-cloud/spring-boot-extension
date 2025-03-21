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

package com.livk.commons.jackson.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.util.JsonMapperUtils;
import com.livk.commons.util.BeanLambda;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author livk
 */
class NumberJsonSerializerTest {

	@Test
	void test() {
		Big big = new Big();
		big.l = 33L;
		big.d = 0.333333d;
		big.f = 0.333333f;
		big.count = BigDecimal.valueOf(0.333333);
		big.sunCount = BigDecimal.valueOf(0.333333);
		String json = JsonMapperUtils.writeValueAsString(big);
		Big bean = JsonMapperUtils.readValue(json, Big.class);
		assertNotNull(bean);
		assertEquals(33L, bean.l);
		assertEquals(0.33d, bean.d);
		assertEquals(0.3f, bean.f);
		assertEquals(0.33d, bean.count.doubleValue());
		assertEquals(0.333d, bean.sunCount.doubleValue());
		JsonNode jsonNode = JsonMapperUtils.readTree(json);
		assertEquals("0033", jsonNode.get(BeanLambda.fieldName(Big::getL)).asText());
		assertEquals(0.33d, jsonNode.get(BeanLambda.fieldName(Big::getD)).asDouble());
		assertEquals(0.3d, jsonNode.get(BeanLambda.fieldName(Big::getF)).asDouble());
		assertEquals(0.33d, jsonNode.get(BeanLambda.fieldName(Big::getCount)).asDouble());
		assertEquals(0.333d, jsonNode.get(BeanLambda.fieldName(Big::getSunCount)).asDouble());
	}

	@Getter
	private static class Big {

		@NumberJsonFormat(pattern = "0000")
		private long l;

		@NumberJsonFormat
		private double d;

		@NumberJsonFormat(pattern = "#0.0")
		private float f;

		@NumberJsonFormat
		private BigDecimal count;

		@NumberJsonFormat(pattern = "#0.000")
		private BigDecimal sunCount;

	}

}
