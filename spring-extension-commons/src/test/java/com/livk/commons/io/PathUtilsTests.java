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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class PathUtilsTests {

	@TempDir
	Path tempDir;

	@Test
	void download() throws IOException {
		InputStream inputStream = new ByteArrayInputStream("livk".getBytes());
		Path path = tempDir.resolve("username.txt");
		PathUtils.download(inputStream, path.toString());
		assertThat(path).exists();
		assertThat(Files.readString(path)).isEqualTo("livk");
	}

	@Test
	void createNewFile() throws IOException {
		Path path = tempDir.resolve("file.txt");
		PathUtils.createNewFile(path);
		assertThat(path).exists();
	}

	@Test
	void gzip() throws IOException {
		try (InputStream inputStream = new ClassPathResource("data.json").getInputStream()) {
			String data = JsonMapperUtils.readTree(inputStream).toString();
			Path path = tempDir.resolve("data.gzip");
			PathUtils.createNewFile(path);
			try (OutputStream outputStream = Files.newOutputStream(path)) {
				GzipUtils.compress(data.getBytes(), outputStream);
			}
			try (InputStream inputStream2 = Files.newInputStream(path)) {
				String copyData = new String(GzipUtils.decompress(inputStream2));
				assertThat(copyData).isEqualTo(data);
			}
		}
	}

}
