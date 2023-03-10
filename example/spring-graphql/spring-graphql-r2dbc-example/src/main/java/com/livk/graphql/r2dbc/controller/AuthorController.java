package com.livk.graphql.r2dbc.controller;

import com.livk.commons.bean.util.BeanUtils;
import com.livk.graphql.r2dbc.entity.Author;
import com.livk.graphql.r2dbc.entity.dto.AuthorDTO;
import com.livk.graphql.r2dbc.repository.AuthorRepository;
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
 */
@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    @MutationMapping
    public Mono<Author> createAuthor(@Argument AuthorDTO dto) {
        return authorRepository.save(BeanUtils.copy(dto, Author.class));
    }
}
