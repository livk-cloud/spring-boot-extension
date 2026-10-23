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
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class PathUtilsTests {

	@TempDir
	Path tempDir;

	@Test
	void download() throws IOException {
		InputStream inputStream = new ByteArrayInputStream("livk".getBytes(StandardCharsets.UTF_8));
		Path path = tempDir.resolve("username.txt");
		PathUtils.download(inputStream, path.toString());
		assertThat(path).exists();
		assertThat(Files.readString(path)).isEqualTo("livk");
	}

	@Test
	void downloadCreatesParentDirectories() throws IOException {
		Path path = tempDir.resolve("nested/directories/username.txt");
		PathUtils.download(new ByteArrayInputStream("livk".getBytes(StandardCharsets.UTF_8)), path.toString());
		assertThat(path).isRegularFile();
		assertThat(Files.readString(path)).isEqualTo("livk");
	}

	@Test
	void downloadWithoutParentDirectory() throws IOException {
		Path path = Path.of("path-utils-download-no-parent.txt");
		try {
			Files.deleteIfExists(path);
			PathUtils.download(new ByteArrayInputStream("livk".getBytes(StandardCharsets.UTF_8)), path.toString());
			assertThat(path).isRegularFile();
			assertThat(Files.readString(path)).isEqualTo("livk");
		}
		finally {
			Files.deleteIfExists(path);
		}
	}

	@Test
	void downloadReplacesExistingFile() throws IOException {
		Path path = tempDir.resolve("username.txt");
		Files.writeString(path, "old", StandardCharsets.UTF_8);
		PathUtils.download(new ByteArrayInputStream("new".getBytes(StandardCharsets.UTF_8)), path.toString());
		assertThat(Files.readString(path)).isEqualTo("new");
	}

	@Test
	void createNewFile() throws IOException {
		Path path = tempDir.resolve("file.txt");
		PathUtils.createNewFile(path);
		assertThat(path).isRegularFile();
	}

	@Test
	void createNewFileCreatesParentDirectories() throws IOException {
		Path path = tempDir.resolve("nested/directories/file.txt");
		PathUtils.createNewFile(path);
		assertThat(path).isRegularFile();
	}

	@Test
	void createNewFileWithoutParentDirectory() throws IOException {
		Path path = Path.of("path-utils-create-no-parent.txt");
		try {
			Files.deleteIfExists(path);
			PathUtils.createNewFile(path);
			assertThat(path).isRegularFile();
		}
		finally {
			Files.deleteIfExists(path);
		}
	}

	@Test
	void createNewFileThrowsWhenFileAlreadyExists() throws IOException {
		Path path = tempDir.resolve("file.txt");
		Files.createFile(path);
		assertThatThrownBy(() -> PathUtils.createNewFile(path)).isInstanceOf(FileAlreadyExistsException.class);
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
