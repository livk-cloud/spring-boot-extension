package com.livk.doc.webflux.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * <p>
 * DocController
 * </p>
 *
 * @author livk
 */
@Tag(name = "/文档")
@RestController
@RequestMapping("doc")
public class DocController {

    @GetMapping
    @Operation(summary = "测试API")
    public Mono<HttpEntity<String>> get() {
        return Mono.just(ResponseEntity.ok("hello world"));
    }
}
