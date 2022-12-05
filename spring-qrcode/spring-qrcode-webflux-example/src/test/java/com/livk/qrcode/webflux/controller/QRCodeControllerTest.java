package com.livk.qrcode.webflux.controller;

import com.livk.autoconfigure.qrcode.enums.PicType;
import com.livk.commons.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * QRCodeControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest
@AutoConfigureWebTestClient(timeout = "15000")
class QRCodeControllerTest {

    @Autowired
    WebTestClient client;

    @Test
    void hello() {
        client.get()
                .uri("/qrcode")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Resource.class)
                .value(resource -> {
                    try {
                        FileUtils.testDownload(resource.getInputStream(), "hello." + PicType.JPG.name().toLowerCase());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        File outFile = new File("hello." + PicType.JPG.name().toLowerCase());
        assertTrue(outFile.exists());
        assertTrue(outFile.delete());
    }

    @Test
    void helloMono() {
        client.get()
                .uri("/qrcode/mono")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Resource.class)
                .value(resource -> {
                    try {
                        FileUtils.testDownload(resource.getInputStream(), "helloMono." + PicType.JPG.name().toLowerCase());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        File outFile = new File("helloMono." + PicType.JPG.name().toLowerCase());
        assertTrue(outFile.exists());
        assertTrue(outFile.delete());
    }

    @Test
    void json() {
        client.get()
                .uri("/qrcode/json")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Resource.class)
                .value(resource -> {
                    try {
                        FileUtils.testDownload(resource.getInputStream(), "json." + PicType.JPG.name().toLowerCase());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        File outFile = new File("json." + PicType.JPG.name().toLowerCase());
        assertTrue(outFile.exists());
        assertTrue(outFile.delete());
    }

    @Test
    void jsonMono() {
        client.get()
                .uri("/qrcode/json/mono")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Resource.class)
                .value(resource -> {
                    try {
                        FileUtils.testDownload(resource.getInputStream(), "jsonMono." + PicType.JPG.name().toLowerCase());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        File outFile = new File("jsonMono." + PicType.JPG.name().toLowerCase());
        assertTrue(outFile.exists());
        assertTrue(outFile.delete());
    }
}
