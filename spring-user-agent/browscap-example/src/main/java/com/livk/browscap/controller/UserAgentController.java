package com.livk.browscap.controller;

import com.blueconic.browscap.Capabilities;
import com.livk.browscap.annotation.UserAgent;
import com.livk.browscap.support.ReactiveUserAgentContextHolder;
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
    public Mono<HttpEntity<Map<String, Capabilities>>> get(@UserAgent Mono<Capabilities> capabilities) {
        return ReactiveUserAgentContextHolder.get()
                .concatWith(capabilities)
                .collect(Collectors.toMap(c -> UUID.randomUUID().toString(), Function.identity()))
                .map(ResponseEntity::ok);
    }
}
