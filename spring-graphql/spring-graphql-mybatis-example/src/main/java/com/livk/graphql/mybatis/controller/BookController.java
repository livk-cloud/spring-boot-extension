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

import java.util.List;

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
    public List<Book> bookList() {
        return bookMapper.list();
    }

    @QueryMapping
    public Book bookByIsbn(@Argument String isbn) {
        return bookMapper.getByIsbn(isbn);
    }

    @SchemaMapping(typeName = "Book", field = "author")
    public Author getAuthor(Book book) {
        return authorMapper.getByIdCardNo(book.getAuthorIdCardNo());
    }

    @MutationMapping
    public Boolean createBook(@Argument BookDTO dto) {
        Book book = new Book();
        BeanUtils.copyProperties(dto, book);
        return bookMapper.save(book) != 0;
    }
}

