package com.livk.r2dbc.service;

import com.livk.r2dbc.domain.User;
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

    Mono<User> getById(Mono<Long> id);

    Mono<Void> save(Mono<User> userMono);

    Mono<Void> updateById(Mono<Long> id, Mono<User> userMono);

    Mono<Void> deleteById(Mono<Long> id);

}
