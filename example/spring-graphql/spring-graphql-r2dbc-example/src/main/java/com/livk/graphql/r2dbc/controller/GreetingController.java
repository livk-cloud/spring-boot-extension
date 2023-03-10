package com.livk.graphql.r2dbc.controller;

import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * <p>
 * GreetingController
 * </p>
 *
 * @author livk
 */
@Controller
public class GreetingController {
    @SubscriptionMapping
    public Flux<String> greetings() {
        return Flux.fromStream(Stream.generate(() -> "Hello GraphQL " + UUID.randomUUID()))
                .delayElements(Duration.ofSeconds(5))
                .take(10);
    }
}
