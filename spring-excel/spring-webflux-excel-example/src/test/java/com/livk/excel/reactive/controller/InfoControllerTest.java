package com.livk.excel.reactive.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.autoconfigure.excel.annotation.ExcelReturn;
import com.livk.util.FileUtils;
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
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * InfoControllerTest
 * </p>
 *
 * @author livk
 * @date 2022/11/2
 */
@SpringBootTest("spring.main.web-application-type=reactive")
@AutoConfigureWebTestClient(timeout = "15000")
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
                        FileUtils.testDownload(resource.getInputStream(), "uploadDownLoad" + ExcelReturn.Suffix.XLS.getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        File outFile = new File("./uploadDownLoad" + ExcelReturn.Suffix.XLS.getName());
        assertTrue(outFile.exists());
        assertTrue(outFile.delete());
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
                        FileUtils.testDownload(resource.getInputStream(), "uploadDownLoadMono" + ExcelReturn.Suffix.XLS.getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        File outFile = new File("./uploadDownLoadMono" + ExcelReturn.Suffix.XLS.getName());
        assertTrue(outFile.exists());
        assertTrue(outFile.delete());
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
                        FileUtils.testDownload(resource.getInputStream(), "uploadDownLoadFlux" + ExcelReturn.Suffix.XLS.getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        File outFile = new File("./uploadDownLoadFlux" + ExcelReturn.Suffix.XLS.getName());
        assertTrue(outFile.exists());
        assertTrue(outFile.delete());
    }
}
