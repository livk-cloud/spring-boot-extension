package com.livk.redisearch.webflux.controller;

import com.livk.commons.jackson.JacksonUtils;
import com.livk.redisearch.webflux.entity.Student;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.reactive.RedisModulesReactiveCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * <p>
 * StudentController
 * </p>
 *
 * @author livk
 * @date 2023/2/3
 */
@RestController
@RequestMapping("student")
@RequiredArgsConstructor
public class StudentController {

    private final StatefulRedisModulesConnection<String, String> connection;

    @GetMapping
    public HttpEntity<Flux<Student>> list(@RequestParam(defaultValue = "*") String query) {
        RedisModulesReactiveCommands<String, String> search = connection.reactive();
        Flux<Student> flux = search.ftSearch(Student.INDEX, query)
                .flatMapMany(Flux::fromIterable)
                .map(document -> JacksonUtils.mapToBean(document, Student.class));
        return ResponseEntity.ok(flux);
    }
}
