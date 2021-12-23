package com.livk.redis.controller;

import com.livk.common.redis.domain.LivkMessage;
import com.livk.common.redis.supprot.LivkReactiveRedisTemplate;
import com.livk.common.redis.supprot.LivkRedisTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                .convertAndSend(LivkMessage.CHANNEL, new LivkMessage().setId(id).setMsg(msg).setData(data))
                .flatMap(n -> Mono.empty());
    }
}
