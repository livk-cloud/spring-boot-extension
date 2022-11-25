package com.livk.graphql.r2dbc.repository;

import com.livk.graphql.r2dbc.entity.Book;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

/**
 * <p>
 * BookRepository
 * </p>
 *
 * @author livk
 * @date 2022/11/25
 */
public interface BookRepository extends R2dbcRepository<Book, Long> {
    Mono<Book> findByIsbn(String isbn);
}
