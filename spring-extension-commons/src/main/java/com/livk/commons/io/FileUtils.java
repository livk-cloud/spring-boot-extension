/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.commons.io;

import com.livk.commons.util.ObjectUtils;
import lombok.experimental.UtilityClass;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * The type File utils.
 */
@UtilityClass
public class FileUtils extends FileCopyUtils {

    /**
     * Download.
     *
     * @param stream   the stream
     * @param filePath the file path
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
        } else {
            throw new IOException();
        }
    }

    /**
     * Create new file boolean.
     *
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
     * Read string.
     *
     * @param file the file
     * @return the string
     * @throws IOException the io exception
     */
    public String read(File file) throws IOException {
        try (FileReader fileReader = new FileReader(file);
             BufferedReader reader = new BufferedReader(fileReader)) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * Gets part values.
     *
     * @param name     the name
     * @param exchange the exchange
     * @return the part values
     */
    public Mono<Part> getPartValues(String name, ServerWebExchange exchange) {
        return exchange.getMultipartData()
                .mapNotNull(multiValueMap -> multiValueMap.getFirst(name));
    }


    /**
     * Gzip compress.
     *
     * @param bytes        the bytes
     * @param outputStream the output stream
     */
    public static void gzipCompress(byte[] bytes, OutputStream outputStream) {
        if (!ObjectUtils.isEmpty(bytes)) {
            try (GZIPOutputStream stream = new GZIPOutputStream(outputStream)) {
                stream.write(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Gzip decompress byte [ ].
     *
     * @param inputStream the input stream
     * @return the byte [ ]
     */
    public static byte[] gzipDecompress(InputStream inputStream) {
        try (GZIPInputStream stream = new GZIPInputStream(inputStream)) {
            return FileUtils.copyToByteArray(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
