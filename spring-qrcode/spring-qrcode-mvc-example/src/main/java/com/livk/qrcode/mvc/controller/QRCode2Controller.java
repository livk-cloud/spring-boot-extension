package com.livk.qrcode.mvc.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.autoconfigure.qrcode.annotation.QRController;
import com.livk.commons.jackson.JacksonUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public String text(String text) {
        return text;
    }

    @PostMapping("json")
    public Map<String, String> json(@RequestBody JsonNode node) {
        return JacksonUtils.convertValueMap(node, String.class, String.class);
    }
}
