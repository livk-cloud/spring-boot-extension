package com.livk.redis.controller;

import com.livk.autoconfigure.redis.supprot.UniversalReactiveRedisTemplate;
import com.livk.common.redis.domain.LivkMessage;
import com.livk.redis.entity.Person;
import com.livk.redis.repository.PersonRepository;
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

    private final UniversalReactiveRedisTemplate universalReactiveRedisTemplate;

    private final PersonRepository personRepository;

    @PostMapping("/redis/{id}")
    public Mono<Void> send(@PathVariable("id") Long id, @RequestParam("msg") String msg,
                           @RequestBody Map<String, Object> data) {
        return universalReactiveRedisTemplate
                .convertAndSend(LivkMessage.CHANNEL, LivkMessage.of().setId(id).setMsg(msg).setData(data))
                .flatMap(n -> Mono.empty());
    }

    @PostMapping("/redis/stream")
    public Mono<Void> stream() {
        return universalReactiveRedisTemplate.opsForStream()
                .add(StreamRecords.newRecord()
                        .ofObject("livk-object")
                        .withStreamKey("livk-streamKey"))
                .flatMap(n -> Mono.empty());
    }

    @PostMapping("/redis/hyper-log-log")
    public Mono<Void> add(@RequestParam Object data) {
        return universalReactiveRedisTemplate.opsForHyperLogLog()
                .add("log", data)
                .flatMap(n -> Mono.empty());
    }

    @GetMapping("/redis/hyper-log-log")
    public Mono<Long> get() {
        return universalReactiveRedisTemplate.opsForHyperLogLog()
                .size("log");
    }

    @PostMapping("person")
    public Mono<Void> add(@RequestBody Mono<Person> personMono) {
        return personMono.doOnNext(personRepository::save).then();
    }
}
