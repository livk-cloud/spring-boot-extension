package com.livk.http.example.controller;

import com.livk.http.example.http.RemoteService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class HttpController {

    private final RemoteService service;

    @PostConstruct
    public void init() {
        log.info("get length:{}", service.get().trim().length());
    }

    @GetMapping("get")
    public HttpEntity<String> get() {
        return ResponseEntity.ok(service.get());
    }

}
