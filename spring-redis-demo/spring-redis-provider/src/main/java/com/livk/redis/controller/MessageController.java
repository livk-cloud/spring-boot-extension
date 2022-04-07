package com.livk.redis.controller;

import com.livk.common.redis.domain.LivkMessage;
import com.livk.common.redis.supprot.LivkReactiveRedisTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.ObjectRecord;
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
 * @date 2021/11/26
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final LivkReactiveRedisTemplate livkReactiveRedisTemplate;

    @PostMapping("/redis/{id}")
    public Mono<Void> send(@PathVariable("id") Long id,
                           @RequestParam("msg") String msg,
                           @RequestBody Map<String, Object> data) {
        return livkReactiveRedisTemplate
                .convertAndSend(LivkMessage.CHANNEL, LivkMessage.of().setId(id).setMsg(msg).setData(data))
                .flatMap(n -> Mono.empty());
    }

    @PostMapping("/redis/stream")
    public Mono<Void> stream() {
        ObjectRecord<String, String> record = StreamRecords.newRecord()
                .ofObject("livk-object")
                .withStreamKey("livk-streamKey");
        return livkReactiveRedisTemplate.opsForStream()
                .add(record)
                .flatMap(n -> Mono.empty());
    }
}
