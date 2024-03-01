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

package com.livk.oss.minio.controller;

import com.livk.commons.io.FileUtils;
import com.livk.testcontainers.DockerImageNames;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class OssControllerTest {

	@Container
	@ServiceConnection
	static MinIOContainer minio = new MinIOContainer(DockerImageNames.minio()).withUserName("admin")
		.withPassword("1375632510")
		.withExposedPorts(9000);

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.oss.access-key", minio::getUserName);
		registry.add("spring.oss.secret-key", minio::getPassword);
		registry.add("spring.oss.url", () -> "minio:http://" + minio.getHost() + ":" + minio.getMappedPort(9000));
	}

	@Autowired
	MockMvc mockMvc;

	@Test
	void test() throws Exception {
		String url = mockMvc.perform(get("/oss/test"))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn()
			.getResponse()
			.getContentAsString();
		try (InputStream stream = new URI(url).toURL().openStream()) {
			FileUtils.download(stream, "./test.jpg");
		}
		File file = new File("./test.jpg");
		assertTrue(file.exists());
		assertTrue(file.delete());
	}

}
