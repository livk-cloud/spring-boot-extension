package com.livk.doc;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * DocController
 * </p>
 *
 * @author livk
 * @date 2022/7/23
 */
@RestController
@RequestMapping("doc")
public class DocController {

    @GetMapping
    public HttpEntity<String> get(@RequestParam String name) {
        return ResponseEntity.ok("hello " + name);
    }
}
