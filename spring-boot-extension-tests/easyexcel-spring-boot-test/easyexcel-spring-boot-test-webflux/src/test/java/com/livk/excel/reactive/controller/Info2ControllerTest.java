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

package com.livk.excel.reactive.controller;

import com.livk.commons.io.FileUtils;
import com.livk.core.easyexcel.annotation.ResponseExcel;
import com.livk.excel.reactive.entity.Info;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * Info2ControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest("spring.main.web-application-type=reactive")
@AutoConfigureWebTestClient(timeout = "15000")
class Info2ControllerTest {

	static MultipartBodyBuilder builder = new MultipartBodyBuilder();

	@Autowired
	WebTestClient client;

	@BeforeAll
	public static void before() {
		builder.part("file", new ClassPathResource("outFile.xls")).filename("file");
	}

	@Test
	void uploadAndDownload() throws IOException {
		Resource resource = client.post()
			.uri("/info/uploadAndDownload")
			.bodyValue(builder.build())
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(Resource.class)
			.returnResult()
			.getResponseBody();

		assertNotNull(resource);
		FileUtils.download(resource.getInputStream(), "./infoUploadDownLoad" + ResponseExcel.Suffix.XLSM.getName());
		File outFile = new File("./infoUploadDownLoad" + ResponseExcel.Suffix.XLSM.getName());
		assertTrue(outFile.exists());
		assertTrue(outFile.delete());
	}

	@Test
	void download() throws IOException {
		List<Info> infos = LongStream.rangeClosed(1, 100)
			.mapToObj(i -> new Info(i, String.valueOf(13_000_000_000L + i)))
			.toList();
		Resource resource = client.post()
			.uri("/info/download")
			.bodyValue(infos)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(Resource.class)
			.returnResult()
			.getResponseBody();

		assertNotNull(resource);
		FileUtils.download(resource.getInputStream(), "./infoDownload" + ResponseExcel.Suffix.XLSM.getName());
		File outFile = new File("./infoDownload" + ResponseExcel.Suffix.XLSM.getName());
		assertTrue(outFile.exists());
		assertTrue(outFile.delete());
	}

}
