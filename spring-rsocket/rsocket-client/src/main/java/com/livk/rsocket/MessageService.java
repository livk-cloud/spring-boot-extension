package com.livk.rsocket;

import com.livk.rsocket.common.entity.Message;
import org.springframework.messaging.rsocket.service.RSocketExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * <p>
 * MessageService
 * </p>
 *
 * @author livk
 * @date 2023/1/4
 */
public interface MessageService {

    @RSocketExchange("request-response")
    Mono<Message> requestResponse(Message request);

    @RSocketExchange("fire-and-forget")
    Mono<Void> fireAndForget(Message request);

    @RSocketExchange("stream")
    Flux<Message> stream(Message request);

    @RSocketExchange("channel")
    Flux<Message> channel(Flux<Duration> settings);
}
