package com.livk.qrcode.webflux.controller;

import com.livk.autoconfigure.qrcode.enums.PicType;
import com.livk.autoconfigure.qrcode.util.QRCodeUtils;
import com.livk.commons.io.FileUtils;
import com.livk.commons.jackson.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
class QRCode2ControllerTest {

    @Autowired
    WebTestClient client;

    String text = "Hello World!";

    String json = JacksonUtils.writeValueAsString(Map.of("username", "root", "password", "root"));

    @Test
    void text() throws IOException {
        client.get()
                .uri(uriBuilder -> uriBuilder.path("/qrcode2")
                        .queryParam("text", text)
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Resource.class)
                .value(resource -> {
                    try {
                        FileUtils.download(resource.getInputStream(), "./text." + PicType.JPG.name().toLowerCase());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        File outFile = new File("text." + PicType.JPG.name().toLowerCase());
        try (FileInputStream inputStream = new FileInputStream(outFile)) {
            assertEquals(text, QRCodeUtils.parseQRCode(inputStream));
        }
        assertTrue(outFile.exists());
        assertTrue(outFile.delete());
    }

    @Test
    void textMono() throws IOException {
        client.get()
                .uri(uriBuilder -> uriBuilder.path("/qrcode2/mono")
                        .queryParam("text", text)
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Resource.class)
                .value(resource -> {
                    try {
                        FileUtils.download(resource.getInputStream(), "./textMono." + PicType.JPG.name().toLowerCase());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        File outFile = new File("textMono." + PicType.JPG.name().toLowerCase());
        try (FileInputStream inputStream = new FileInputStream(outFile)) {
            assertEquals(text, QRCodeUtils.parseQRCode(inputStream));
        }
        assertTrue(outFile.exists());
        assertTrue(outFile.delete());
    }

    @Test
    void json() throws IOException {
        client.post()
                .uri("/qrcode2/json")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Resource.class)
                .value(resource -> {
                    try {
                        FileUtils.download(resource.getInputStream(), "./json." + PicType.JPG.name().toLowerCase());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        File outFile = new File("json." + PicType.JPG.name().toLowerCase());
        try (FileInputStream inputStream = new FileInputStream(outFile)) {
            assertEquals(json, QRCodeUtils.parseQRCode(inputStream));
        }
        assertTrue(outFile.exists());
        assertTrue(outFile.delete());
    }

    @Test
    void jsonMono() throws IOException {
        client.post()
                .uri("/qrcode2/json/mono")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Resource.class)
                .value(resource -> {
                    try {
                        FileUtils.download(resource.getInputStream(), "./jsonMono." + PicType.JPG.name().toLowerCase());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        File outFile = new File("jsonMono." + PicType.JPG.name().toLowerCase());
        try (FileInputStream inputStream = new FileInputStream(outFile)) {
            assertEquals(json, QRCodeUtils.parseQRCode(inputStream));
        }
        assertTrue(outFile.exists());
        assertTrue(outFile.delete());
    }
}
