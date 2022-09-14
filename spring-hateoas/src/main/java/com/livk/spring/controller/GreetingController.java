package com.livk.spring.controller;

import com.livk.spring.domain.Greeting;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * <p>
 * GreetingController
 * </p>
 *
 * @author livk
 * @date 2021/11/24
 */
@RestController
public class GreetingController {

    private static final String TEMPLATE = "hello,%s!";

    @GetMapping("/greeting")
    public Mono<HttpEntity<Greeting>> greeting(
            @RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        Greeting greeting = new Greeting(String.format(TEMPLATE, name));
        greeting.add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).greeting(name)).withSelfRel());
        return Mono.just(ResponseEntity.ok(greeting));
    }

}
