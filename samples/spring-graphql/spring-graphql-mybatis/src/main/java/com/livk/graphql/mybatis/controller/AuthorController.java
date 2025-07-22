/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.graphql.mybatis.controller;

import com.livk.commons.util.BeanUtils;
import com.livk.graphql.mybatis.entity.Author;
import com.livk.graphql.mybatis.entity.dto.AuthorDTO;
import com.livk.graphql.mybatis.mapper.AuthorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

/**
 * @author livk
 */
@Controller
@RequiredArgsConstructor
public class AuthorController {

	private final AuthorMapper authorMapper;

	@MutationMapping
	public Mono<Boolean> createAuthor(@Argument AuthorDTO dto) {
		return Mono.justOrEmpty(authorMapper.save(BeanUtils.copy(dto, Author.class)) != 0);
	}

}
