package com.livk.browscap.controller;

import com.blueconic.browscap.Capabilities;
import com.livk.browscap.annotation.UserAgent;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
    public Mono<HttpEntity<Capabilities>> get(@UserAgent Mono<Capabilities> capabilities) {
//        return Mono.just(ResponseEntity.ok(capabilities));
        return capabilities.map(ResponseEntity::ok);
    }
}
