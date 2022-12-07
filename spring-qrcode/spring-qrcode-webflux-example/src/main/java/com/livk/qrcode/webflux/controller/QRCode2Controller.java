package com.livk.qrcode.webflux.controller;

import com.livk.autoconfigure.qrcode.annotation.QRController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * <p>
 * QRCodeController
 * </p>
 *
 * @author livk
 */
@QRController
@RequestMapping("qrcode2")
public class QRCode2Controller {

    @GetMapping
    public String hello() {
        return "hello world";
    }

    @GetMapping("mono")
    public Mono<String> helloMono() {
        return Mono.just("hello world Mono");
    }

    @GetMapping("json")
    public Map<String, String> json() {
        return Map.of("username", "root", "password", "123456");
    }

    @GetMapping("/json/mono")
    public Mono<Map<String, String>> jsonMono() {
        return Mono.just(Map.of("username", "root", "password", "123456"));
    }
}
