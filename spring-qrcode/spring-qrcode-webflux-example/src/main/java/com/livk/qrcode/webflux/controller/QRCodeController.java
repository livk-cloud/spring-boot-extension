package com.livk.qrcode.webflux.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.autoconfigure.qrcode.annotation.QRCode;
import com.livk.commons.jackson.JacksonUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * <p>
 * QRCodeController
 * </p>
 *
 * @author livk
 */
@RestController
@RequestMapping("qrcode")
public class QRCodeController {

    @QRCode
    @GetMapping
    public String text(String text) {
        return text;
    }

    @QRCode
    @GetMapping("mono")
    public Mono<String> textMono(String text) {
        return Mono.just(text);
    }

    @QRCode
    @PostMapping("json")
    public Map<String, String> json(@RequestBody JsonNode node) {
        return JacksonUtils.convertValueMap(node, String.class, String.class);
    }

    @QRCode
    @PostMapping("/json/mono")
    public Mono<Map<String, String>> jsonMono(@RequestBody JsonNode node) {
        return Mono.just(JacksonUtils.convertValueMap(node, String.class, String.class));
    }
}
