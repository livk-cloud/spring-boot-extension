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

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Utility class for path operations.
 *
 * @author livk
 */
@UtilityClass
public class PathUtils {

	/**
	 * Downloads content from an InputStream to a file.
	 * <p>
	 * 路径不存在则自动创建.
	 * @param stream the stream
	 * @param filePath 文件路径
	 * @throws IOException the io exception
	 */
	public void download(InputStream stream, String filePath) throws IOException {
		Path path = Path.of(filePath);
		Path parent = path.getParent();
		if (parent != null) {
			Files.createDirectories(parent);
		}
		Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * Creates a new file, auto-creating parent directories if needed.
	 * @param path the path
	 * @throws IOException the io exception
	 */
	public void createNewFile(Path path) throws IOException {
		Path parent = path.getParent();
		if (parent != null) {
			Files.createDirectories(parent);
		}
		Files.createFile(path);
	}

}
