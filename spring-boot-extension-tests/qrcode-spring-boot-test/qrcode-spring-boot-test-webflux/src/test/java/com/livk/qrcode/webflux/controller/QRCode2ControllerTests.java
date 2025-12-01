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

package com.livk.qrcode.webflux.controller;

import com.livk.commons.io.FileUtils;
import com.livk.commons.jackson.JsonMapperUtils;
import com.livk.context.qrcode.PicType;
import com.livk.context.qrcode.QrCodeManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureWebTestClient(timeout = "15000")
class QRCode2ControllerTests {

	@Autowired
	WebTestClient client;

	@Autowired
	QrCodeManager qrCodeManager;

	final String text = "Hello World!";

	final String json = JsonMapperUtils.writeValueAsString(Map.of("username", "root", "password", "root"));

	@Test
	void text() throws IOException {
		Resource resource = client.get()
			.uri(uriBuilder -> uriBuilder.path("/qrcode2").queryParam("text", text).build())
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(Resource.class)
			.returnResult()
			.getResponseBody();

		assertThat(resource).isNotNull();
		FileUtils.download(resource.getInputStream(), "./text." + PicType.JPG.name().toLowerCase());
		File outFile = new File("text." + PicType.JPG.name().toLowerCase());
		try (FileInputStream inputStream = new FileInputStream(outFile)) {
			assertThat(qrCodeManager.parser(inputStream)).isEqualTo(text);
		}
		assertThat(outFile).exists().isFile();
		assertThat(outFile.delete()).isTrue();
	}

	@Test
	void textMono() throws IOException {
		Resource resource = client.get()
			.uri(uriBuilder -> uriBuilder.path("/qrcode2/mono").queryParam("text", text).build())
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(Resource.class)
			.returnResult()
			.getResponseBody();

		assertThat(resource).isNotNull();
		FileUtils.download(resource.getInputStream(), "./textMono." + PicType.JPG.name().toLowerCase());
		File outFile = new File("textMono." + PicType.JPG.name().toLowerCase());
		try (FileInputStream inputStream = new FileInputStream(outFile)) {
			assertThat(qrCodeManager.parser(inputStream)).isEqualTo(text);
		}
		assertThat(outFile).exists().isFile();
		assertThat(outFile.delete()).isTrue();
	}

	@Test
	void json() throws IOException {
		Resource resource = client.post()
			.uri("/qrcode2/json")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(json)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(Resource.class)
			.returnResult()
			.getResponseBody();

		assertThat(resource).isNotNull();
		FileUtils.download(resource.getInputStream(), "./json." + PicType.JPG.name().toLowerCase());
		File outFile = new File("json." + PicType.JPG.name().toLowerCase());
		try (FileInputStream inputStream = new FileInputStream(outFile)) {
			assertThat(qrCodeManager.parser(inputStream)).isEqualTo(json);
		}
		assertThat(outFile).exists().isFile();
		assertThat(outFile.delete()).isTrue();
	}

	@Test
	void jsonMono() throws IOException {
		Resource resource = client.post()
			.uri("/qrcode2/json/mono")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(json)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(Resource.class)
			.returnResult()
			.getResponseBody();

		assertThat(resource).isNotNull();
		FileUtils.download(resource.getInputStream(), "./jsonMono." + PicType.JPG.name().toLowerCase());
		File outFile = new File("jsonMono." + PicType.JPG.name().toLowerCase());
		try (FileInputStream inputStream = new FileInputStream(outFile)) {
			assertThat(qrCodeManager.parser(inputStream)).isEqualTo(json);
		}
		assertThat(outFile).exists().isFile();
		assertThat(outFile.delete()).isTrue();
	}

}
