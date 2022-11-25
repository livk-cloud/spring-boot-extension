package com.livk.graphql.r2dbc.controller;

import com.livk.graphql.r2dbc.entity.Author;
import com.livk.graphql.r2dbc.entity.Book;
import com.livk.graphql.r2dbc.entity.dto.BookDTO;
import com.livk.graphql.r2dbc.repository.AuthorRepository;
import com.livk.graphql.r2dbc.repository.BookRepository;
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

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    @QueryMapping
    public Flux<Book> bookList() {
        return bookRepository.findAll();
    }

    @QueryMapping
    public Mono<Book> bookByIsbn(@Argument String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @SchemaMapping(typeName = "Book", field = "author")
    public Mono<Author> getAuthor(Book book) {
        return authorRepository.findByIdCardNo(book.getAuthorIdCardNo());
    }

    @MutationMapping
    public Mono<Book> createBook(@Argument BookDTO dto) {
        Book book = new Book();
        BeanUtils.copyProperties(dto, book);
        return bookRepository.save(book);
    }
}

