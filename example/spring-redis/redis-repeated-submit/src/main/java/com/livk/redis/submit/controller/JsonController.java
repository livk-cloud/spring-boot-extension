package com.livk.redis.submit.controller;

import com.livk.redis.submit.annotation.AutoRepeatedSubmit;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author livk
 */
@RestController
@RequestMapping("json")
public class JsonController {

    @AutoRepeatedSubmit
    @GetMapping
    public HttpEntity<String> get() {
        return ResponseEntity.ok("json");
    }
}
