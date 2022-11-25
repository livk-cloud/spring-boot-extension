package com.livk.graphql.r2dbc.controller;

import com.livk.graphql.r2dbc.entity.Author;
import com.livk.graphql.r2dbc.entity.dto.AuthorDTO;
import com.livk.graphql.r2dbc.repository.AuthorRepository;
import com.livk.util.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

/**
 * <p>
 * AuthorController
 * </p>
 *
 * @author livk
 * @date 2022/11/25
 */
@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    @MutationMapping
    public Mono<Author> createAuthor(@Argument AuthorDTO dto) {
        Author author = new Author();
        BeanUtils.copyProperties(dto, author);
        return authorRepository.save(author);
    }
}
