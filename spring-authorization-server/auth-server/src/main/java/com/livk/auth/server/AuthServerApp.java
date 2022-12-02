package com.livk.auth.server;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * AuthServerApp
 * </p>
 *
 * @author livk
 * @date 2022/4/24
 */
@RestController
@SpringBootApplication
public class AuthServerApp {

    public static void main(String[] args) {
        LivkSpring.run(AuthServerApp.class, args);
    }

    @GetMapping("hello")
    public HttpEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }
}
