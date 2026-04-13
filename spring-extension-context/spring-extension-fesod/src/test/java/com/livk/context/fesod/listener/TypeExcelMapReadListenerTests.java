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

package com.livk.context.fesod.listener;

import com.livk.context.fesod.Info;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class TypeExcelMapReadListenerTests {

	@Test
	void executePopulatesMapData() throws IOException {
		TypeExcelMapReadListener<Info> listener = new TypeExcelMapReadListener<>();
		try (InputStream inputStream = new ClassPathResource("outFile.xls").getInputStream()) {
			listener.execute(inputStream, Info.class, true);
		}
		Map<String, ? extends List<Info>> map = listener.toMapData();
		assertThat(map).hasSize(1).containsKey("0");
		assertThat(map.get("0")).hasSize(100);
	}

	@Test
	void toListDataFlattensAllSheets() throws IOException {
		TypeExcelMapReadListener<Info> listener = new TypeExcelMapReadListener<>();
		try (InputStream inputStream = new ClassPathResource("outFile.xls").getInputStream()) {
			listener.execute(inputStream, Info.class, true);
		}
		List<Info> list = listener.toListData();
		assertThat(list).hasSize(100);
	}

	@Test
	void emptyListenerReturnsEmptyData() {
		TypeExcelMapReadListener<Info> listener = new TypeExcelMapReadListener<>();
		assertThat(listener.toMapData()).isEmpty();
		assertThat(listener.toListData()).isEmpty();
	}

}
