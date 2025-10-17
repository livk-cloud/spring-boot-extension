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

package com.livk.context.fastexcel.listener;

import com.livk.context.fastexcel.Info;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class TypeExcelMapReadListenerTests {

	static final ExcelMapReadListener<Info> listener = new TypeExcelMapReadListener<>();

	@Test
	void test() throws IOException {
		InputStream inputStream = new ClassPathResource("outFile.xls").getInputStream();
		listener.execute(inputStream, Info.class, true);

		Map<String, ? extends Collection<Info>> map = listener.getMapData();
		assertThat(map).isNotEmpty();
		assertThat(map).hasSize(1);
		assertThat(map).containsKey("0");
		assertThat(map.values().stream().mapToLong(Collection::size).sum()).isEqualTo(100);
	}

}
