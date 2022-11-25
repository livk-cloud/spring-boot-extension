package com.livk.graphql.r2dbc.repository;

import com.livk.graphql.r2dbc.entity.Author;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

/**
 * <p>
 * AuthorRepository
 * </p>
 *
 * @author livk
 * @date 2022/11/25
 */
public interface AuthorRepository extends R2dbcRepository<Author, Long> {
    Mono<Author> findByIdCardNo(String authorIdCardNo);
}
