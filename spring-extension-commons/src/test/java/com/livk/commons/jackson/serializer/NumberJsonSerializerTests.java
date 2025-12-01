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

package com.livk.commons.jackson.serializer;

import com.livk.commons.jackson.JsonMapperUtils;
import com.livk.commons.util.BeanLambda;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * @author livk
 */
class NumberJsonSerializerTests {

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

		assertThat(bean).isNotNull();
		assertThat(bean.l).isEqualTo(33L);

		assertThat(bean.d).isCloseTo(0.33d, within(0.0001));
		assertThat(bean.f).isCloseTo(0.3f, within(0.0001f));
		assertThat(bean.count.doubleValue()).isCloseTo(0.33d, within(0.0001));
		assertThat(bean.sunCount.doubleValue()).isCloseTo(0.333d, within(0.0001));

		JsonNode jsonNode = JsonMapperUtils.readTree(json);

		assertThat(jsonNode.get(BeanLambda.fieldName(Big::getL)).asString()).isEqualTo("0033");
		assertThat(jsonNode.get(BeanLambda.fieldName(Big::getD)).asDouble()).isCloseTo(0.33d, within(0.0001));
		assertThat(jsonNode.get(BeanLambda.fieldName(Big::getF)).asDouble()).isCloseTo(0.3d, within(0.0001));
		assertThat(jsonNode.get(BeanLambda.fieldName(Big::getCount)).asDouble()).isCloseTo(0.33d, within(0.0001));
		assertThat(jsonNode.get(BeanLambda.fieldName(Big::getSunCount)).asDouble()).isCloseTo(0.333d, within(0.0001));

	}

	@Getter
	private static final class Big {

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
