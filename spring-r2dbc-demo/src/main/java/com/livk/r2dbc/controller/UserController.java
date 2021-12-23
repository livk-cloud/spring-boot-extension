package com.livk.r2dbc.controller;

import com.livk.r2dbc.domain.User;
import com.livk.r2dbc.repository.UserRepository;
import com.livk.r2dbc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 * UserController
 * </p>
 *
 * @author livk
 * @date 2021/12/6
 */
@Slf4j
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public HttpEntity<Flux<User>> users() {
        return ResponseEntity.ok(userService.list());
    }

    @GetMapping("/{id}")
    public HttpEntity<Mono<User>> user(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getById(Mono.just(id)));
    }

    @PostMapping
    public HttpEntity<Mono<Void>> save(@RequestBody Mono<User> userMono) {
        return ResponseEntity.ok(userService.save(userMono));
    }

    @PutMapping("/{id}")
    public HttpEntity<Mono<Void>> update(@PathVariable("id") Long id,
                                         @RequestBody Mono<User> userMono) {
        return ResponseEntity.ok(userService.updateById(Mono.just(id), userMono));
    }

    @DeleteMapping("/{id}")
    public HttpEntity<Mono<Void>> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.deleteById(Mono.just(id)));
    }
}
