package com.livk.yauaa.webflux.example.controller;

import com.livk.autoconfigure.useragent.annotation.UserAgentInfo;
import com.livk.autoconfigure.useragent.reactive.ReactiveUserAgentContextHolder;
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
 */
@RestController
@RequestMapping("user-agent")
public class UserAgentController {

    @GetMapping
    public Mono<HttpEntity<Map<String, Map<String, String>>>> get(@UserAgentInfo Mono<UserAgent> userAgentMono) {
        return userAgentMono.concatWith(ReactiveUserAgentContextHolder.get().map(wrapper -> wrapper.cast(UserAgent.class)))
                .map(userAgent -> userAgent
                        .getAvailableFieldNamesSorted()
                        .stream()
                        .collect(Collectors.toMap(Function.identity(), userAgent::getValue)))
                .collect(Collectors.toMap(c -> UUID.randomUUID().toString(), Function.identity()))
                .map(ResponseEntity::ok);
    }
}
