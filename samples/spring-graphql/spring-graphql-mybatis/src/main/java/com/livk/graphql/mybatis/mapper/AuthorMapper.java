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

import com.livk.graphql.mybatis.entity.Author;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author livk
 */
@Mapper
public interface AuthorMapper {

	@Insert("insert into author(id_card_no, name, age) values (#{idCardNo},#{name},#{age})")
	int save(Author author);

	@Delete("truncate table author")
	void clear();

	@Select("select * from author where id_card_no = #{authorIdCardNo}")
	Author getByIdCardNo(@Param("authorIdCardNo") String authorIdCardNo);

}
