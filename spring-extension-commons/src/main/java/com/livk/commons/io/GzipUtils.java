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
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZip压缩/解压缩工具类
 *
 * @author livk
 */
@UtilityClass
public class GzipUtils {

	/**
	 * 使用GZip进行压缩
	 * <p>
	 * 把数据压缩至OutputStream
	 * @param bytes 待压缩数据
	 * @param outputStream 输出流
	 * @throws IOException io exception
	 */
	public static void compress(byte[] bytes, OutputStream outputStream) throws IOException {
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
	public static byte[] decompress(InputStream inputStream) throws IOException {
		try (GZIPInputStream stream = new GZIPInputStream(inputStream)) {
			return FileCopyUtils.copyToByteArray(stream);
		}
	}

}
