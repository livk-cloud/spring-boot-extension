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

package com.livk.context.easyexcel;

import com.livk.context.easyexcel.annotation.ResponseExcel;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author livk
 */
class EasyExcelSupportTest {

	@Test
	void read() throws IOException {
		Info.InfoMapReadListener listener = new Info.InfoMapReadListener();
		InputStream inputStream = new ClassPathResource("outFile.xls").getInputStream();
		EasyExcelSupport.read(inputStream, Info.class, listener, true);

		Map<String, ? extends Collection<Info>> map = listener.getMapData();
		assertFalse(map.isEmpty());

		assertEquals(1, map.size());
		assertEquals(1, map.keySet().size());
		assertEquals(Set.of("0"), map.keySet());
		assertEquals(100, map.values().stream().mapToLong(Collection::size).sum());
	}

	@Test
	void write() {
		Map<String, List<Info>> data = Map.of("0", List.of(new Info("123456789"), new Info("987654321")));
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		assertEquals(0, stream.toByteArray().length);

		EasyExcelSupport.write(stream, Info.class, null, data);

		assertNotEquals(0, stream.toByteArray().length);
	}

	@Test
	void fileName() throws NoSuchMethodException {
		ResponseExcel responseExcel = this.getClass()
			.getDeclaredMethod("fileNameResponse")
			.getAnnotation(ResponseExcel.class);
		assertEquals("outFile.xls", EasyExcelSupport.fileName(responseExcel));
	}

	@ResponseExcel(fileName = "outFile", suffix = ResponseExcel.Suffix.XLS)
	void fileNameResponse() {

	}

}
