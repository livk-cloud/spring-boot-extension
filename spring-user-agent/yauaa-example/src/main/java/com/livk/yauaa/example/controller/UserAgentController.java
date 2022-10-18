package com.livk.yauaa.example.controller;

import com.livk.yauaa.support.ReactiveUserAgentContextHolder;
import nl.basjes.parse.useragent.UserAgent;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * UserAgentController
 * </p>
 *
 * @author livk
 * @date 2022/10/6
 */
@RestController
@RequestMapping("user-agent")
public class UserAgentController {

    @GetMapping
    public Mono<HttpEntity<Map<String, UserAgent>>> get(@com.livk.yauaa.annotation.UserAgent Mono<UserAgent> userAgent) {
        return ReactiveUserAgentContextHolder.get()
                .concatWith(userAgent)
                .collect(Collectors.toMap(c -> UUID.randomUUID().toString(), Function.identity()))
                .map(ResponseEntity::ok);
    }
}
