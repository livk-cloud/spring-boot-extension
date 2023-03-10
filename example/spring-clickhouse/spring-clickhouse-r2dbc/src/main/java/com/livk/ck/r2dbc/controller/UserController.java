package com.livk.ck.r2dbc.controller;

import com.livk.ck.r2dbc.entity.User;
import com.livk.ck.r2dbc.service.UserService;
import lombok.RequiredArgsConstructor;
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
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public HttpEntity<Flux<User>> users() {
        return ResponseEntity.ok(userService.list());
    }

    @PostMapping
    public HttpEntity<Mono<Void>> save(@RequestBody Mono<User> userMono) {
        return ResponseEntity.ok(userService.save(userMono));
    }

    @DeleteMapping("/{id}")
    public HttpEntity<Mono<Void>> delete(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(userService.remove(Mono.just(id)));
    }

}
