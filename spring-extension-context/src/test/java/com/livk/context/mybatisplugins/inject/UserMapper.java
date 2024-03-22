/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.mybatisplugins.inject;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * UserMapper
 * </p>
 *
 * @author livk
 */
@Mapper
interface UserMapper {

	@Select("select * from `user` where id = #{id}")
	User getById(@Param("id") Integer id);

	@Insert("insert into `user`(username, insert_time, update_time) "
			+ "values(#{user.username}, #{user.insertTime}, #{user.updateTime})")
	void insert(@Param("user") User user);

}
