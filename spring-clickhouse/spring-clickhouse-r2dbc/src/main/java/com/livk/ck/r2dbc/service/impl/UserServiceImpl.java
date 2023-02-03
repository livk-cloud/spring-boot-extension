package com.livk.ck.r2dbc.service.impl;

import com.livk.ck.r2dbc.entity.User;
import com.livk.ck.r2dbc.repository.UserRepository;
import com.livk.ck.r2dbc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 * UserServiceImpl
 * </p>
 *
 * @author livk
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Flux<User> list() {
        return userRepository.findAll();
    }

    @Override
    public Mono<Void> remove(Mono<Integer> id) {
        return userRepository.deleteById(id);
    }

    @Override
    public Mono<Void> save(Mono<User> userMono) {
        return userMono.flatMap(userRepository::save);
    }
}
