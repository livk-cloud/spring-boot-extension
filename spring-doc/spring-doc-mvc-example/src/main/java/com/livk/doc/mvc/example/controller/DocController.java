package com.livk.doc.mvc.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Operation(summary = "hello 你好")
    public HttpEntity<String> get() {
        return ResponseEntity.ok("hello world");
    }
}
