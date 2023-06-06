/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.qrcode.webflux.controller;

import com.livk.autoconfigure.qrcode.enums.PicType;
import com.livk.autoconfigure.qrcode.util.QRCodeUtils;
import com.livk.commons.io.FileUtils;
import com.livk.commons.jackson.util.JsonMapperUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * QRCodeControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest
@AutoConfigureWebTestClient(timeout = "15000")
class QRCodeControllerTest {

	@Autowired
	WebTestClient client;

	String text = "Hello World!";

	String json = JsonMapperUtils.writeValueAsString(Map.of("username", "root", "password", "root"));

	@Test
	void text() throws IOException {
		client.get()
			.uri(uriBuilder -> uriBuilder.path("/qrcode")
				.queryParam("text", text)
				.build())
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(Resource.class)
			.value(resource -> {
				try {
					FileUtils.download(resource.getInputStream(), "./text." + PicType.JPG.name().toLowerCase());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		File outFile = new File("text." + PicType.JPG.name().toLowerCase());
		try (FileInputStream inputStream = new FileInputStream(outFile)) {
			assertEquals(text, QRCodeUtils.parseQRCode(inputStream));
		}
		assertTrue(outFile.exists());
		assertTrue(outFile.delete());
	}

	@Test
	void textMono() throws IOException {
		client.get()
			.uri(uriBuilder -> uriBuilder.path("/qrcode/mono")
				.queryParam("text", text)
				.build())
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(Resource.class)
			.value(resource -> {
				try {
					FileUtils.download(resource.getInputStream(), "./textMono." + PicType.JPG.name().toLowerCase());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		File outFile = new File("textMono." + PicType.JPG.name().toLowerCase());
		try (FileInputStream inputStream = new FileInputStream(outFile)) {
			assertEquals(text, QRCodeUtils.parseQRCode(inputStream));
		}
		assertTrue(outFile.exists());
		assertTrue(outFile.delete());
	}

	@Test
	void textCode() throws IOException {
		client.get()
			.uri(uriBuilder -> uriBuilder.path("/qrcode/entity")
				.queryParam("text", text)
				.build())
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(Resource.class)
			.value(resource -> {
				try {
					FileUtils.download(resource.getInputStream(), "./text." + PicType.JPG.name().toLowerCase());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		File outFile = new File("text." + PicType.JPG.name().toLowerCase());
		try (FileInputStream inputStream = new FileInputStream(outFile)) {
			assertEquals(text, QRCodeUtils.parseQRCode(inputStream));
		}
		assertTrue(outFile.exists());
		assertTrue(outFile.delete());
	}

	@Test
	void json() throws IOException {
		client.post()
			.uri("/qrcode/json")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(json)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(Resource.class)
			.value(resource -> {
				try {
					FileUtils.download(resource.getInputStream(), "./json." + PicType.JPG.name().toLowerCase());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		File outFile = new File("json." + PicType.JPG.name().toLowerCase());
		try (FileInputStream inputStream = new FileInputStream(outFile)) {
			assertEquals(json, QRCodeUtils.parseQRCode(inputStream));
		}
		assertTrue(outFile.exists());
		assertTrue(outFile.delete());
	}

	@Test
	void jsonMono() throws IOException {
		client.post()
			.uri("/qrcode/json/mono")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(json)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(Resource.class)
			.value(resource -> {
				try {
					FileUtils.download(resource.getInputStream(), "./jsonMono." + PicType.JPG.name().toLowerCase());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		File outFile = new File("jsonMono." + PicType.JPG.name().toLowerCase());
		try (FileInputStream inputStream = new FileInputStream(outFile)) {
			assertEquals(json, QRCodeUtils.parseQRCode(inputStream));
		}
		assertTrue(outFile.exists());
		assertTrue(outFile.delete());
	}

	@Test
	void jsonCode() throws IOException {
		client.post()
			.uri("/qrcode/entity/json")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(json)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(Resource.class)
			.value(resource -> {
				try {
					FileUtils.download(resource.getInputStream(), "./json." + PicType.JPG.name().toLowerCase());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		File outFile = new File("json." + PicType.JPG.name().toLowerCase());
		try (FileInputStream inputStream = new FileInputStream(outFile)) {
			assertEquals(json, QRCodeUtils.parseQRCode(inputStream));
		}
		assertTrue(outFile.exists());
		assertTrue(outFile.delete());
	}
}
