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

package com.livk.commons.io;

import com.livk.commons.util.ObjectUtils;
import lombok.experimental.UtilityClass;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 文件相关工具类
 *
 * @author livk
 */
@UtilityClass
public class FileUtils extends FileCopyUtils {

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

	/**
	 * 读取文件转成String
	 * @param file the file
	 * @return the string
	 * @throws IOException the io exception
	 */
	public String read(File file) throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
			return reader.lines().collect(Collectors.joining("\n"));
		}
	}

	/**
	 * 从ServerWebExchange读取文件转成Mono Part
	 * @param name 文件参数
	 * @param exchange the exchange
	 * @return the part values
	 */
	public Mono<Part> getPartValues(String name, ServerWebExchange exchange) {
		return exchange.getMultipartData().mapNotNull(multiValueMap -> multiValueMap.getFirst(name));
	}

	/**
	 * 使用GZip进行压缩
	 * <p>
	 * 把数据压缩至OutputStream
	 * @param bytes 待压缩数据
	 * @param outputStream 输出流
	 * @throws IOException io exception
	 */
	public static void gzipCompress(byte[] bytes, OutputStream outputStream) throws IOException {
		if (!ObjectUtils.isEmpty(bytes)) {
			try (GZIPOutputStream stream = new GZIPOutputStream(outputStream)) {
				stream.write(bytes);
			}
		}
	}

	/**
	 * 使用GZip进行解压缩
	 * @param inputStream 输入流
	 * @return byte[]
	 * @throws IOException io exception
	 */
	public static byte[] gzipDecompress(InputStream inputStream) throws IOException {
		try (GZIPInputStream stream = new GZIPInputStream(inputStream)) {
			return FileUtils.copyToByteArray(stream);
		}
	}

}
