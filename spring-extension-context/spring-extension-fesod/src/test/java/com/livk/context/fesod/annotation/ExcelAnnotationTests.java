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

package com.livk.context.fesod.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class ExcelAnnotationTests {

	@Test
	void excelControllerComposesControllerAndResponseExcel() {
		assertThat(ExcelController.class).hasAnnotation(Controller.class);
		assertThat(ExcelController.class.getAnnotation(ResponseExcel.class).fileName()).isEqualTo("out");
	}

	@Test
	void excelParamDefaultFileName() throws Exception {
		assertThat(ExcelParam.class.getDeclaredMethod("fileName").getDefaultValue()).isEqualTo("file");
	}

	@Test
	void requestExcelDefaultIgnoreEmptyRow() throws Exception {
		assertThat(RequestExcel.class.getDeclaredMethod("ignoreEmptyRow").getDefaultValue()).isEqualTo(false);
	}

	@Test
	void responseExcelSuffixNames() {
		assertThat(ResponseExcel.Suffix.XLS.getName()).isEqualTo(".xls");
		assertThat(ResponseExcel.Suffix.XLSX.getName()).isEqualTo(".xlsx");
		assertThat(ResponseExcel.Suffix.XLSM.getName()).isEqualTo(".xlsm");
	}

	@Test
	void responseExcelParseNameUsesTemplateSuffixWhenPresent() throws Exception {
		ResponseExcel responseExcel = TemplateController.class.getDeclaredMethod("download")
			.getAnnotation(ResponseExcel.class);

		assertThat(ResponseExcel.Utils.parseName(responseExcel)).isEqualTo("result.xlsx");
	}

	@Test
	void responseExcelParseNameUsesConfiguredSuffixWhenTemplateMissing() throws Exception {
		ResponseExcel responseExcel = SuffixController.class.getDeclaredMethod("download")
			.getAnnotation(ResponseExcel.class);

		assertThat(ResponseExcel.Utils.parseName(responseExcel)).isEqualTo("result.xls");
	}

	static class TemplateController {

		@ResponseExcel(fileName = "result", template = "classpath:/template.xlsx")
		void download() {
		}

	}

	static class SuffixController {

		@ResponseExcel(fileName = "result", suffix = ResponseExcel.Suffix.XLS)
		void download() {
		}

	}

}
