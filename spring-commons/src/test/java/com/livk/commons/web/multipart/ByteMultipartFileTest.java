package com.livk.commons.web.multipart;

import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * ByteMultipartFileTest
 * </p>
 *
 * @author livk
 * @date 2023/2/1
 */
class ByteMultipartFileTest {
    @Test
    public void test() throws IOException {
        byte[] bytes = "Test String".getBytes();
        MultipartFile multipartFile = new ByteMultipartFile("test", bytes);
        assertFalse(multipartFile.isEmpty());
        assertArrayEquals(bytes, multipartFile.getBytes());
        assertEquals(bytes.length, multipartFile.getSize());
        assertEquals("test", multipartFile.getName());
        assertTrue(multipartFile.getInputStream() instanceof ByteArrayInputStream);
    }
}
