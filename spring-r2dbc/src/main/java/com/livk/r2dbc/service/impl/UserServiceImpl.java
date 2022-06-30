package com.livk.r2dbc.service.impl;

import com.livk.r2dbc.domain.User;
import com.livk.r2dbc.repository.UserRepository;
import com.livk.r2dbc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * <p>
 * UserServiceImpl
 * </p>
 *
 * @author livk
 * @date 2021/12/13
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
    public Mono<User> getById(Mono<Long> id) {
        return userRepository.findById(id);
    }

    @Override
    public Mono<Void> save(Mono<User> userMono) {
        return userMono.flatMap(user -> userRepository.save(user).flatMap(
                u1 -> Objects.nonNull(u1.id()) ? Mono.empty() : Mono.defer(() -> Mono.error(new RuntimeException()))));
    }

    @Override
    public Mono<Void> updateById(Mono<Long> id, Mono<User> userMono) {
        return userMono.flatMap(monoUser -> userRepository.findById(id)
                        .switchIfEmpty(Mono.error(new RuntimeException("Id Not Found!")))
                        .flatMap(user -> userRepository.save(new User(user.id(), monoUser.username(), monoUser.password()))))
                .then();
    }

    @Override
    public Mono<Void> deleteById(Mono<Long> id) {
        return userRepository.deleteById(id);
    }

}
