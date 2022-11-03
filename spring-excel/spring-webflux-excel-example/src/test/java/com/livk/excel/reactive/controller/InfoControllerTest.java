package com.livk.excel.reactive.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.autoconfigure.excel.annotation.ExcelReturn;
import com.livk.util.LogUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;

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
 * InfoControllerTest
 * </p>
 *
 * @author livk
 * @date 2022/11/2
 */
@SpringBootTest("spring.main.web-application-type=reactive")
@AutoConfigureWebTestClient
class InfoControllerTest {

    static MultipartBodyBuilder builder = new MultipartBodyBuilder();
    @Autowired
    WebTestClient client;

    @BeforeAll
    public static void before() {
        builder.part("file", new ClassPathResource("outFile.xls"))
                .filename("file");
    }

    @Test
    void upload() {
        client.post()
                .uri("/upload")
                .bodyValue(builder.build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(JsonNode.class)
                .value(System.out::println);
    }

    @Test
    void uploadMono() {
        client.post()
                .uri("/uploadMono")
                .bodyValue(builder.build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(JsonNode.class)
                .value(System.out::println);
    }

    @Test
    void uploadDownLoadMono() {
        client.post()
                .uri("/uploadDownLoad")
                .bodyValue(builder.build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Resource.class)
                .value(resource -> {
                    try {
                        down(resource.getInputStream(), "uploadDownLoad" + ExcelReturn.Suffix.XLS.getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Test
    void testUploadDownLoadMono() {
        client.post()
                .uri("/uploadDownLoadMono")
                .bodyValue(builder.build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Resource.class)
                .value(resource -> {
                    try {
                        down(resource.getInputStream(), "uploadDownLoadMono" + ExcelReturn.Suffix.XLS.getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Test
    void uploadDownLoadFlux() {
        client.post()
                .uri("/uploadDownLoadFlux")
                .bodyValue(builder.build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Resource.class)
                .value(resource -> {
                    try {
                        down(resource.getInputStream(), "uploadDownLoadFlux" + ExcelReturn.Suffix.XLS.getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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
