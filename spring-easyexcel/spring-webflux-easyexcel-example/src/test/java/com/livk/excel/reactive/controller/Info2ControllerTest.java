package com.livk.excel.reactive.controller;

import com.livk.autoconfigure.easyexcel.annotation.ExcelReturn;
import com.livk.commons.util.FileUtils;
import com.livk.excel.reactive.entity.Info;
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
import java.util.List;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * Info2ControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest("spring.main.web-application-type=reactive")
@AutoConfigureWebTestClient(timeout = "15000")
class Info2ControllerTest {

    static MultipartBodyBuilder builder = new MultipartBodyBuilder();
    @Autowired
    WebTestClient client;

    @BeforeAll
    public static void before() {
        builder.part("file", new ClassPathResource("outFile.xls"))
                .filename("file");
    }

    @Test
    void uploadAndDownload() {
        client.post()
                .uri("/info/uploadAndDownload")
                .bodyValue(builder.build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Resource.class)
                .value(resource -> {
                    try {
                        FileUtils.testDownload(resource.getInputStream(), "./infoUploadDownLoad" + ExcelReturn.Suffix.XLSM.getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        File outFile = new File("./infoUploadDownLoad" + ExcelReturn.Suffix.XLSM.getName());
        assertTrue(outFile.exists());
        assertTrue(outFile.delete());
    }

    @Test
    void download() {
        List<Info> infos = LongStream.rangeClosed(1, 100)
                .mapToObj(i -> new Info(i, 13_000_000_000L + i + ""))
                .toList();
        client.post()
                .uri("/info/download")
                .bodyValue(infos)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Resource.class)
                .value(resource -> {
                    try {
                        FileUtils.testDownload(resource.getInputStream(), "./infoDownload" + ExcelReturn.Suffix.XLSM.getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        File outFile = new File("./infoDownload" + ExcelReturn.Suffix.XLSM.getName());
        assertTrue(outFile.exists());
        assertTrue(outFile.delete());
    }
}
