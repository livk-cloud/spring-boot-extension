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

package com.livk.context.fesod;

import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class ExcelDataTypeTests {

	@Test
	void matchListReturnsListType() {
		assertThat(ExcelDataType.match(List.class)).isEqualTo(ExcelDataType.LIST);
		assertThat(ExcelDataType.match(ArrayList.class)).isEqualTo(ExcelDataType.LIST);
	}

	@Test
	void matchMapReturnsMapType() {
		assertThat(ExcelDataType.match(Map.class)).isEqualTo(ExcelDataType.MAP);
		assertThat(ExcelDataType.match(HashMap.class)).isEqualTo(ExcelDataType.MAP);
	}

	@Test
	void matchUnsupportedTypeThrows() {
		assertThatThrownBy(() -> ExcelDataType.match(String.class)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void applyListResolvesGenericType() {
		ResolvableType type = ResolvableType.forClassWithGenerics(List.class, Info.class);
		assertThat(ExcelDataType.LIST.apply(type)).isEqualTo(Info.class);
	}

	@Test
	void applyMapResolvesSecondGenericType() {
		ResolvableType innerType = ResolvableType.forClassWithGenerics(List.class, Info.class);
		ResolvableType type = ResolvableType.forClassWithGenerics(Map.class, ResolvableType.forClass(String.class),
				innerType);
		assertThat(ExcelDataType.MAP.apply(type)).isEqualTo(Info.class);
	}

	@Test
	void enumValuesCount() {
		assertThat(ExcelDataType.values()).hasSize(2);
	}

}
