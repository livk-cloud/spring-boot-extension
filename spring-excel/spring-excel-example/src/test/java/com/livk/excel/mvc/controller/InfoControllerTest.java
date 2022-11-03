package com.livk.excel.mvc.controller;

import com.livk.autoconfigure.excel.annotation.ExcelReturn;
import com.livk.util.LogUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * InfoControllerTest
 * </p>
 *
 * @author livk
 * @date 2022/11/2
 */
@AutoConfigureMockMvc
@SpringBootTest
class InfoControllerTest {

    @Autowired
    MockMvc mockMvc;

    ClassPathResource resource = new ClassPathResource("outFile.xls");

    MockMultipartFile file = new MockMultipartFile("file", "outFile.xlsx", MediaType.MULTIPART_FORM_DATA_VALUE, resource.getInputStream());

    InfoControllerTest() throws IOException {
    }

    @Test
    void uploadList() throws Exception {
        mockMvc.perform(multipart(POST, "/uploadList")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void uploadAndDownload() throws Exception {
        mockMvc.perform(multipart(POST, "/uploadAndDownload")
                        .file(file))
                .andExpect(status().isOk())
                .andDo(result -> {
                    ByteArrayInputStream in = new ByteArrayInputStream(result.getResponse().getContentAsByteArray());
                    down(in, "uploadAndDownloadMock" + ExcelReturn.Suffix.XLS.getName());
                });
    }

    private void down(InputStream stream, String name) throws IOException {
        String path = "./" + name;
        File file = new File(path);
        if (!file.exists()) {
            LogUtils.info("开始创建文件");
            int count = 0;
            while (!file.createNewFile()) {
                if (++count == 3) {
                    throw new IOException();
                }
            }
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            FileChannel channel = fileOutputStream.getChannel();
            ReadableByteChannel readableByteChannel = Channels.newChannel(stream);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (readableByteChannel.read(buffer) != -1) {
                buffer.flip();
                channel.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
