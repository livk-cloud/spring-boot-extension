package com.livk.redis.controller;

import com.livk.autoconfigure.redis.supprot.LivkReactiveRedisTemplate;
import com.livk.common.redis.domain.LivkMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * <p>
 * MessageController
 * </p>
 *
 * @author livk
 */
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final LivkReactiveRedisTemplate livkReactiveRedisTemplate;

    @PostMapping("/redis/{id}")
    public Mono<Void> send(@PathVariable("id") Long id, @RequestParam("msg") String msg,
                           @RequestBody Map<String, Object> data) {
        return livkReactiveRedisTemplate
                .convertAndSend(LivkMessage.CHANNEL, LivkMessage.of().setId(id).setMsg(msg).setData(data))
                .flatMap(n -> Mono.empty());
    }

    @PostMapping("/redis/stream")
    public Mono<Void> stream() {
        return livkReactiveRedisTemplate.opsForStream()
                .add(StreamRecords.newRecord()
                        .ofObject("livk-object")
                        .withStreamKey("livk-streamKey"))
                .flatMap(n -> Mono.empty());
    }

    @PostMapping("/redis/hyper-log-log")
    public Mono<Void> add(@RequestParam Object data) {
        return livkReactiveRedisTemplate.opsForHyperLogLog()
                .add("log", data)
                .flatMap(n -> Mono.empty());
    }

    @GetMapping("/redis/hyper-log-log")
    public Mono<Long> get() {
        return livkReactiveRedisTemplate.opsForHyperLogLog()
                .size("log");
    }
}
