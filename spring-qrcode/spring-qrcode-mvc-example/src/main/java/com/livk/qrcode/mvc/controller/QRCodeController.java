package com.livk.qrcode.mvc.controller;

import com.livk.autoconfigure.qrcode.annotation.QRCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    @QRCode
    public String hello() {
        return "hello world";
    }

    @GetMapping("json")
    @QRCode
    public Map<String, String> json() {
        return Map.of("username", "root", "password", "123456");
    }
}
