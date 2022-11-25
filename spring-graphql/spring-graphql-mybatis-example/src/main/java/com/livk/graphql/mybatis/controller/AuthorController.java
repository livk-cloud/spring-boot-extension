package com.livk.graphql.mybatis.controller;

import com.livk.graphql.mybatis.entity.Author;
import com.livk.graphql.mybatis.entity.dto.AuthorDTO;
import com.livk.graphql.mybatis.mapper.AuthorMapper;
import com.livk.util.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

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

    private final AuthorMapper authorMapper;

    @MutationMapping
    public Boolean createAuthor(@Argument AuthorDTO dto) {
        Author author = new Author();
        BeanUtils.copyProperties(dto, author);
        return authorMapper.save(author) != 0;
    }
}
