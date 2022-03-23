package com.livk.security.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * HelloController
 * </p>
 *
 * @author livk
 * @date 2022/3/23
 */
@RestController
public class HelloController {

    @GetMapping("hello")
    public HttpEntity<String> hello(){
        return ResponseEntity.ok("hello");
    }

    @GetMapping("index")
    public HttpEntity<String> index(){
        return ResponseEntity.ok("index");
    }
}
