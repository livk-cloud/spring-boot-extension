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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * 文件相关工具类
 *
 * @author livk
 */
@UtilityClass
public class FileUtils {

	/**
	 * 文件下载
	 * <p>
	 * 路径不存在则自动创建
	 * @param stream the stream
	 * @param filePath 文件路径
	 * @throws IOException the io exception
	 */
	public void download(InputStream stream, String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists() || createNewFile(file)) {
			try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
				FileChannel channel = fileOutputStream.getChannel();
				ReadableByteChannel readableByteChannel = Channels.newChannel(stream);
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				while (readableByteChannel.read(buffer) != -1) {
					buffer.flip();
					channel.write(buffer);
					buffer.clear();
				}
			}
		}
		else {
			throw new IOException();
		}
	}

	/**
	 * 创建文件
	 * <p>
	 * 路径不存在则自动创建
	 * @param file the file
	 * @return the boolean
	 * @throws IOException the io exception
	 */
	public boolean createNewFile(File file) throws IOException {
		boolean flag = true;
		File parentFile = file.getParentFile();
		if (parentFile != null && !parentFile.exists()) {
			flag = parentFile.mkdirs();
		}
		return flag && file.createNewFile();
	}

}
