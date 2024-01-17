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

package com.livk.ck.jdbc.mapper;

import com.livk.ck.jdbc.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * UserMapper
 * </p>
 *
 * @author livk
 */
@Mapper
public interface UserMapper {

	@Select("select id, app_id, version, reg_time from user")
	List<User> selectList();

	@Delete("alter table user delete where reg_time=#{regTime}")
	int delete(@Param("regTime") String regTime);

	@Insert("insert into user values (#{id},#{appId},#{version},#{regTime})")
	int insert(User user);

}
