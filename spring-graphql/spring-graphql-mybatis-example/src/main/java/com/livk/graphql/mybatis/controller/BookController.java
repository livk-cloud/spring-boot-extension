package com.livk.graphql.mybatis.controller;

import com.livk.graphql.mybatis.entity.Author;
import com.livk.graphql.mybatis.entity.Book;
import com.livk.graphql.mybatis.entity.dto.BookDTO;
import com.livk.graphql.mybatis.mapper.AuthorMapper;
import com.livk.graphql.mybatis.mapper.BookMapper;
import com.livk.util.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 * BookController
 * </p>
 *
 * @author livk
 * @date 2022/11/25
 */
@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookMapper bookMapper;

    private final AuthorMapper authorMapper;

    @QueryMapping
    public Flux<Book> bookList() {
        return Flux.fromIterable(bookMapper.list());
    }

    @QueryMapping
    public Mono<Book> bookByIsbn(@Argument String isbn) {
        return Mono.justOrEmpty(bookMapper.getByIsbn(isbn));
    }

    @SchemaMapping(typeName = "Book", field = "author")
    public Mono<Author> getAuthor(Book book) {
        return Mono.justOrEmpty(authorMapper.getByIdCardNo(book.getAuthorIdCardNo()));
    }

    @MutationMapping
    public Mono<Boolean> createBook(@Argument BookDTO dto) {
        return Mono.justOrEmpty(bookMapper.save(BeanUtils.copy(dto, Book.class)) != 0);
    }
}

