package com.livk.qrcode.mvc.controller;

import com.livk.autoconfigure.qrcode.annotation.QRController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("json")
    public Map<String, String> json() {
        return Map.of("username", "root", "password", "123456");
    }
}
