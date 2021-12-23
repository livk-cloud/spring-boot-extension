package com.livk.auth.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * IndexController
 * </p>
 *
 * @author livk
 * @date 2021/12/22
 */
@RestController
@RequestMapping("index")
public class IndexController {

    @GetMapping
    public HttpEntity<String> index() {
        return ResponseEntity.ok("index");
    }
}
