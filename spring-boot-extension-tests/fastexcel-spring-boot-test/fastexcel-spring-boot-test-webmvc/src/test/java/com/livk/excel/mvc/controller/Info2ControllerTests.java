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

package com.livk.excel.mvc.controller;

import com.livk.commons.io.FileUtils;
import com.livk.commons.jackson.JsonMapperUtils;
import com.livk.context.fastexcel.annotation.ResponseExcel;
import com.livk.excel.mvc.entity.Info;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@AutoConfigureMockMvc
@SpringBootTest({ "spring.datasource.driver-class-name=org.h2.Driver", "spring.datasource.url=jdbc:h2:mem:test",
		"spring.sql.init.mode=never" })
class Info2ControllerTests {

	@Autowired
	MockMvcTester tester;

	final ClassPathResource resource = new ClassPathResource("outFile.xls");

	final MockMultipartFile file = new MockMultipartFile("file", "outFile.xls", MediaType.MULTIPART_FORM_DATA_VALUE,
			resource.getInputStream());

	Info2ControllerTests() throws IOException {
	}

	@Test
	void uploadAndDownload() throws Exception {
		byte[] body = tester.post()
			.uri("/info/uploadAndDownload")
			.multipart()
			.file(file)
			.assertThat()
			.hasStatusOk()
			.body()
			.actual();
		FileUtils.download(new ByteArrayInputStream(body),
				"./infoUploadAndDownloadMock" + ResponseExcel.Suffix.XLSM.getName());
		File outFile = new File("./infoUploadAndDownloadMock" + ResponseExcel.Suffix.XLSM.getName());
		assertThat(outFile).exists().isFile();
		assertThat(outFile.delete()).isTrue();
	}

	@Test
	void download() throws Exception {
		List<Info> infos = LongStream.rangeClosed(1, 100)
			.mapToObj(i -> new Info(i, String.valueOf(13_000_000_000L + i)))
			.toList();
		byte[] body = tester.post()
			.uri("/info/download")
			.contentType(MediaType.APPLICATION_JSON)
			.content(JsonMapperUtils.writeValueAsString(infos))
			.assertThat()
			.hasStatusOk()
			.body()
			.actual();
		FileUtils.download(new ByteArrayInputStream(body), "./infoDownload" + ResponseExcel.Suffix.XLSM.getName());
		File outFile = new File("./infoDownload" + ResponseExcel.Suffix.XLSM.getName());
		assertThat(outFile).exists().isFile();
		assertThat(outFile.delete()).isTrue();
	}

}
