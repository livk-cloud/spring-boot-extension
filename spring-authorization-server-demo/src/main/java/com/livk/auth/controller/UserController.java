package com.livk.auth.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.livk.auth.domain.Users;
import com.livk.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * <p>
 * UserController
 * </p>
 *
 * @author livk
 * @date 2021/12/22
 */
@Slf4j
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public HttpEntity<?> save(@RequestBody Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return ResponseEntity.ok(userService.save(users));
    }
}
