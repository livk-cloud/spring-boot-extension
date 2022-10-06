package com.livk.example;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * AopController
 * </p>
 *
 * @author livk
 * @date 2022/7/5
 */
@AopAnno
@RestController
public class AopController {

    @AopAnno
    @GetMapping("aop")
    public HttpEntity<String> aop() {
        return ResponseEntity.ok("hello world");
    }

    @GetMapping("look")
    public HttpEntity<String> look() {
        return ResponseEntity.ok("hello world ,look");
    }
}
