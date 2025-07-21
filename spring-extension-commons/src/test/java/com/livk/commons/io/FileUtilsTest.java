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

package com.livk.commons.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.JsonMapperUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * FileUtilsTest
 * </p>
 *
 * @author livk
 */
class FileUtilsTest {

	@Test
	void download() throws IOException {
		InputStream inputStream = new ByteArrayInputStream("livk".getBytes());
		FileUtils.download(inputStream, "./username.txt");
		assertTrue(new File("./username.txt").delete());
	}

	@Test
	void createNewFile() throws IOException {
		File file = new File("./file.txt");
		assertTrue(FileUtils.createNewFile(file));
		assertTrue(file.delete());
	}

	@Test
	void gzip() throws IOException {
		InputStream inputStream = new ClassPathResource("data.json").getInputStream();
		String data = JsonMapperUtils.readValue(inputStream, JsonNode.class).toString();
		File file = new File("./data.gzip");
		FileUtils.createNewFile(file);
		try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
			FileUtils.gzipCompress(data.getBytes(), fileOutputStream);
		}

		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			String copyData = new String(FileUtils.gzipDecompress(fileInputStream));
			assertEquals(data, copyData);
			assertTrue(file.delete());
		}
	}

}
