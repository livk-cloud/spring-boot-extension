package com.livk.rsocket;

import com.livk.rsocket.common.entity.Message;
import io.rsocket.RSocket;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * <p>
 * RSocketClient
 * </p>
 *
 * @author livk
 * @date 2022/5/23
 */
@Slf4j
@RestController
public class RSocketClient {

    private final RSocketRequester rsocketRequester;

    public RSocketClient(RSocketRequester.Builder rsocketRequesterBuilder, RSocketStrategies strategies) {
        this.rsocketRequester = rsocketRequesterBuilder.rsocketStrategies(strategies).tcp("localhost", 7000);

        RSocket rsocket = this.rsocketRequester.rsocket();
        if (rsocket != null) {
            rsocket.onClose()
                    .doOnError(error -> log.warn("发生错误，链接关闭"))
                    .doFinally(consumer -> log.info("链接关闭"))
                    .subscribe();
        } else {
            log.info("rsocket is null");
        }
    }

    @PreDestroy
    void shutdown() {
        RSocket rsocket = rsocketRequester.rsocket();
        if (rsocket != null) {
            rsocket.dispose();
        }
    }

    @GetMapping("request-response")
    public Mono<Message> requestResponse() {
        return this.rsocketRequester.route("request-response")
                .data(new Message("客户端", "服务器"))
                .retrieveMono(Message.class)
                .log();
    }

    @GetMapping("fire-and-forget")
    public Mono<String> fireAndForget() {
        return this.rsocketRequester
                .route("fire-and-forget")
                .data(new Message("客户端", "服务器"))
                .send()
                .flatMap(unused -> Mono.just("fire and forget"));
    }

    @GetMapping("stream")
    public Mono<String> stream() {
        return Mono.just("stream");
    }

    @GetMapping("channel")
    public Mono<String> channel() {
        Mono<Duration> setting1 = Mono.just(Duration.ofSeconds(1));
        Mono<Duration> setting2 = Mono.just(Duration.ofSeconds(3)).delayElement(Duration.ofSeconds(5));
        Mono<Duration> setting3 = Mono.just(Duration.ofSeconds(5)).delayElement(Duration.ofSeconds(15));
        Flux<Duration> settings = Flux.concat(setting1, setting2, setting3)
                .doOnNext(d -> log.info("客户端channel发送消息 {}", d.getSeconds()));
        return settings.collectList()
                .flatMap(durations -> Mono.just("channel"));
    }

}
