package com.livk.qrcode.mvc.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.autoconfigure.qrcode.annotation.QRCode;
import com.livk.commons.jackson.JacksonUtils;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("json")
    public Map<String, String> json(@RequestBody JsonNode node) {
        return JacksonUtils.convertValueMap(node, String.class, String.class);
    }
}
