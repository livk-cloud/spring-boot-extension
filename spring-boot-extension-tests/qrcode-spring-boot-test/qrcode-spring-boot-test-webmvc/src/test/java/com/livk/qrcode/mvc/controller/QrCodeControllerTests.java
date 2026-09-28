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

package com.livk.qrcode.mvc.controller;

import com.livk.commons.io.PathUtils;
import com.livk.commons.jackson.JsonMapperUtils;
import com.livk.context.qrcode.PicType;
import com.livk.context.qrcode.QrCodeManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class QrCodeControllerTests {

	@Autowired
	MockMvcTester tester;

	@Autowired
	QrCodeManager qrCodeManager;

	@Test
	void text() throws Exception {
		String text = "Hello World!";
		byte[] body = tester.get().uri("/qrcode").param("text", text).assertThat().hasStatusOk().body().actual();
		PathUtils.download(new ByteArrayInputStream(body), "./text." + PicType.JPG.name().toLowerCase());
		Path path = Path.of("text." + PicType.JPG.name().toLowerCase());
		try (InputStream inputStream = Files.newInputStream(path)) {
			assertThat(qrCodeManager.parser(inputStream)).isEqualTo(text);
		}
		assertThat(path).exists().isRegularFile();
		assertThat(Files.deleteIfExists(path)).isTrue();
		assertThat(path).doesNotExist();
	}

	@Test
	void json() throws Exception {
		String json = JsonMapperUtils.writeValueAsString(Map.of("username", "root", "password", "root"));
		byte[] body = tester.post()
			.uri("/qrcode/json")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json)
			.assertThat()
			.hasStatusOk()
			.body()
			.actual();
		PathUtils.download(new ByteArrayInputStream(body), "./json." + PicType.JPG.name().toLowerCase());
		Path path = Path.of("json." + PicType.JPG.name().toLowerCase());
		try (InputStream inputStream = Files.newInputStream(path)) {
			assertThat(qrCodeManager.parser(inputStream)).isEqualTo(json);
		}
		assertThat(path).exists().isRegularFile();
		assertThat(Files.deleteIfExists(path)).isTrue();
		assertThat(path).doesNotExist();
	}

	@Test
	void textCode() throws Exception {
		String text = "Hello World!";
		byte[] body = tester.get().uri("/qrcode/entity").param("text", text).assertThat().hasStatusOk().body().actual();
		PathUtils.download(new ByteArrayInputStream(body), "./text." + PicType.JPG.name().toLowerCase());
		Path path = Path.of("text." + PicType.JPG.name().toLowerCase());
		try (InputStream inputStream = Files.newInputStream(path)) {
			assertThat(qrCodeManager.parser(inputStream)).isEqualTo(text);
		}
		assertThat(path).exists().isRegularFile();
		assertThat(Files.deleteIfExists(path)).isTrue();
		assertThat(path).doesNotExist();
	}

	@Test
	void jsonCode() throws Exception {
		String json = JsonMapperUtils.writeValueAsString(Map.of("username", "root", "password", "root"));
		byte[] body = tester.post()
			.uri("/qrcode/entity/json")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json)
			.assertThat()
			.hasStatusOk()
			.body()
			.actual();
		PathUtils.download(new ByteArrayInputStream(body), "./json." + PicType.JPG.name().toLowerCase());
		Path path = Path.of("json." + PicType.JPG.name().toLowerCase());
		try (InputStream inputStream = Files.newInputStream(path)) {
			assertThat(qrCodeManager.parser(inputStream)).isEqualTo(json);
		}
		assertThat(path).exists().isRegularFile();
		assertThat(Files.deleteIfExists(path)).isTrue();
		assertThat(path).doesNotExist();
	}

}
