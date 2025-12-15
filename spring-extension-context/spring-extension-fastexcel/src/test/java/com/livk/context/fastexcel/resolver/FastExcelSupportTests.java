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

package com.livk.context.fastexcel.resolver;

import com.livk.context.fastexcel.ExcelDataType;
import com.livk.context.fastexcel.Info;
import com.livk.context.fastexcel.annotation.ResponseExcel;
import com.livk.context.fastexcel.listener.TypeExcelMapReadListener;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class FastExcelSupportTests {

	@Test
	void write() {
		CustomFastExcelSupport support = new CustomFastExcelSupport();
		Map<String, List<Info>> data = Map.of("0", List.of(new Info("123456789"), new Info("987654321")));
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		assertThat(stream.toByteArray()).isEmpty();

		support.write(stream, Info.class, null, data);

		assertThat(stream.toByteArray()).isNotEmpty();
	}

	@Test
	void getExcelData() throws IOException {
		TypeExcelMapReadListener<Info> listener = new TypeExcelMapReadListener<>();
		listener.execute(new ClassPathResource("outFile.xls").getInputStream(), Info.class, true);
		CustomFastExcelSupport support = new CustomFastExcelSupport();
		assertThat(support.getExcelData(listener, ExcelDataType.LIST)).isEqualTo(listener.toListData());
		assertThat(support.getExcelData(listener, ExcelDataType.MAP)).isEqualTo(listener.toMapData());
	}

	@Test
	void fileName() throws NoSuchMethodException {
		ResponseExcel responseExcel = this.getClass()
			.getDeclaredMethod("fileNameResponse")
			.getAnnotation(ResponseExcel.class);
		assertThat(responseExcel).isNotNull();
		assertThat(ResponseExcel.Utils.parseName(responseExcel)).isEqualTo("outFile.xls");
	}

	@ResponseExcel(fileName = "outFile", suffix = ResponseExcel.Suffix.XLS)
	void fileNameResponse() {

	}

	static class CustomFastExcelSupport extends FastExcelSupport {

	}

}
