package com.livk.sso.resource.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * UserController
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("list")
    public HttpEntity<String> list() {
        return ResponseEntity.ok("list");
    }

    @PutMapping("update")
    public HttpEntity<String> update() {
        return ResponseEntity.ok("update");
    }

}
