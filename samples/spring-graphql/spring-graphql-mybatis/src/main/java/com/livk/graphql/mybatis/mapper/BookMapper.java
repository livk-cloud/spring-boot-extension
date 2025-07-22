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

package com.livk.graphql.mybatis.mapper;

import com.livk.graphql.mybatis.entity.Book;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author livk
 */
@Mapper
public interface BookMapper {

	@Select("select * from book")
	List<Book> list();

	@Select("select * from book where isbn = #{isbn}")
	Book getByIsbn(@Param("isbn") String isbn);

	@Delete("truncate table book")
	void clear();

	@Insert("insert into book(isbn, title, pages, author_id_card_no) values (#{isbn},#{title},#{pages},#{authorIdCardNo})")
	int save(Book book);

}
