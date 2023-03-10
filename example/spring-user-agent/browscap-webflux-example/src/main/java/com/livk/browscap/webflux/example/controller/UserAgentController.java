package com.livk.browscap.webflux.example.controller;

import com.blueconic.browscap.Capabilities;
import com.livk.autoconfigure.useragent.annotation.UserAgentInfo;
import com.livk.autoconfigure.useragent.reactive.ReactiveUserAgentContextHolder;
import com.livk.commons.bean.Wrapper;
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
    public Mono<HttpEntity<Map<String, Capabilities>>> get(@UserAgentInfo Mono<Capabilities> capabilities) {
        return ReactiveUserAgentContextHolder.get()
                .concatWith(capabilities.map(Wrapper::of))
                .map(wrapper -> (Capabilities) wrapper.unwrap())
                .collect(Collectors.toMap(c -> UUID.randomUUID().toString(), Function.identity()))
                .map(ResponseEntity::ok);
    }
}
