package com.livk.qrcode.webflux.controller;

import com.livk.autoconfigure.qrcode.annotation.QRCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * <p>
 * QRCodeController
 * </p>
 *
 * @author livk
 * @date 2022/11/4
 */
@RestController
@RequestMapping("qrcode")
public class QRCodeController {

    @GetMapping
    @QRCode
    public String hello() {
        return "hello world";
    }

    @GetMapping("mono")
    @QRCode
    public Mono<String> helloMono() {
        return Mono.just("hello world Mono");
    }

    @GetMapping("json")
    @QRCode
    public Map<String, String> json() {
        return Map.of("username", "root", "password", "123456");
    }

    @GetMapping("/json/mono")
    @QRCode
    public Mono<Map<String, String>> jsonMono() {
        return Mono.just(Map.of("username", "root", "password", "123456"));
    }
}
