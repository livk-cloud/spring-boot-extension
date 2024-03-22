/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.qrcode.mvc.controller;

import com.livk.commons.io.FileUtils;
import com.livk.commons.jackson.util.JsonMapperUtils;
import com.livk.context.qrcode.PicType;
import com.livk.context.qrcode.QRCodeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * QRCodeControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class QRCode2ControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void text() throws Exception {
		String text = "Hello World!";
		mockMvc.perform(get("/qrcode2").param("text", text)).andExpect(status().isOk()).andDo(result -> {
			ByteArrayInputStream in = new ByteArrayInputStream(result.getResponse().getContentAsByteArray());
			FileUtils.download(in, "./text." + PicType.JPG.name().toLowerCase());
		});
		File outFile = new File("text." + PicType.JPG.name().toLowerCase());
		try (FileInputStream inputStream = new FileInputStream(outFile)) {
			assertEquals(text, QRCodeUtils.parseQRCode(inputStream));
		}
		assertTrue(outFile.exists());
		assertTrue(outFile.delete());
	}

	@Test
	void json() throws Exception {
		String json = JsonMapperUtils.writeValueAsString(Map.of("username", "root", "password", "root"));
		mockMvc.perform(post("/qrcode2/json").contentType(MediaType.APPLICATION_JSON).content(json))
			.andExpect(status().isOk())
			.andDo(result -> {
				ByteArrayInputStream in = new ByteArrayInputStream(result.getResponse().getContentAsByteArray());
				FileUtils.download(in, "./json." + PicType.JPG.name().toLowerCase());
			});
		File outFile = new File("json." + PicType.JPG.name().toLowerCase());
		try (FileInputStream inputStream = new FileInputStream(outFile)) {
			assertEquals(json, QRCodeUtils.parseQRCode(inputStream));
		}
		assertTrue(outFile.exists());
		assertTrue(outFile.delete());
	}

}
