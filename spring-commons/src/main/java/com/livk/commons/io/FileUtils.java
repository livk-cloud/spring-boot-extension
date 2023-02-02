package com.livk.commons.io;

import lombok.experimental.UtilityClass;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

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
}
