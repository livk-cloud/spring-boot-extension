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

import com.livk.commons.jackson.JsonMapperUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class FileUtilsTests {

	@TempDir
	Path tempDir;

	@Test
	void download() throws IOException {
		InputStream inputStream = new ByteArrayInputStream("livk".getBytes());
		String filePath = tempDir.resolve("username.txt").toString();
		FileUtils.download(inputStream, filePath);
		File file = new File(filePath);
		assertThat(file).exists();
		assertThat(Files.readString(file.toPath())).isEqualTo("livk");
	}

	@Test
	void createNewFile() throws IOException {
		File file = tempDir.resolve("file.txt").toFile();
		assertThat(FileUtils.createNewFile(file)).isTrue();
		assertThat(file).exists();
	}

	@Test
	void gzip() throws IOException {
		InputStream inputStream = new ClassPathResource("data.json").getInputStream();
		String data = JsonMapperUtils.readTree(inputStream).toString();

		File file = tempDir.resolve("data.gzip").toFile();
		FileUtils.createNewFile(file);
		try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
			FileUtils.gzipCompress(data.getBytes(), fileOutputStream);
		}

		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			String copyData = new String(FileUtils.gzipDecompress(fileInputStream));
			assertThat(copyData).isEqualTo(data);
		}
	}

}
