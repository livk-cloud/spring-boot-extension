package com.livk.auth.server;

import com.livk.commons.spring.SpringLauncher;
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
 */
@RestController
@SpringBootApplication
public class AuthServerApp {

    public static void main(String[] args) {
        SpringLauncher.run(AuthServerApp.class, args);
    }

    @GetMapping("hello")
    public HttpEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }
}
