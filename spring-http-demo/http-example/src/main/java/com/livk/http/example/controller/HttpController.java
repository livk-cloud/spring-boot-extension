package com.livk.http.example.controller;

import com.livk.http.example.http.RemoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * HttpController
 * </p>
 *
 * @author livk
 * @date 2022/5/20
 */
@RestController
@RequiredArgsConstructor
public class HttpController {

    private final RemoteService service;

    @GetMapping
    public HttpEntity<String> get() {
        return ResponseEntity.ok(service.get());
    }

}
