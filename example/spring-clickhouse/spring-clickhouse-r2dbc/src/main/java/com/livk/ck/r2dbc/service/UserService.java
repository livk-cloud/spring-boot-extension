package com.livk.ck.r2dbc.service;

import com.livk.ck.r2dbc.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 * UserService
 * </p>
 *
 * @author livk
 */
public interface UserService {

    Flux<User> list();

    Mono<Void> remove(Mono<Integer> id);

    Mono<Void> save(Mono<User> userMono);
}
