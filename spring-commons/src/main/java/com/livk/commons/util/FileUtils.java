package com.livk.commons.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * <p>
 * FileUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class FileUtils extends FileCopyUtils {

    public void testDownload(InputStream stream, String filePath) throws IOException {
        File file = new File(filePath);
        if (createNewFile(file)) {
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

    @SneakyThrows
    public boolean createNewFile(File file) {
        boolean flag = true;
        if (!file.getParentFile().exists()) {
            flag = file.getParentFile().mkdirs();
        }
        return flag && file.createNewFile();
    }
}
