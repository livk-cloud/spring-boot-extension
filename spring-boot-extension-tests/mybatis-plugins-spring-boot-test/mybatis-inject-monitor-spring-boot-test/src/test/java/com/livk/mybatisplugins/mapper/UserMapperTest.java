/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.mybatisplugins.mapper;

import com.fasterxml.jackson.databind.JavaType;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.livk.commons.bean.domain.PageInfo;
import com.livk.commons.function.FieldFunc;
import com.livk.commons.jackson.util.JsonMapperUtils;
import com.livk.commons.jackson.util.TypeFactoryUtils;
import com.livk.mybatisplugins.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p>
 * UserMapperTest
 * </p>
 *
 * @author livk
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class UserMapperTest {
	@Autowired
	UserMapper userMapper;

	Integer id = 10;


	@Order(1)
	@Test
	public void saveTest() {
		User user = new User();
		user.setId(id);
		user.setUsername("livk");
		int result = userMapper.insert(user);
		assertEquals(1, result);
	}

	@Order(2)
	@Test
	public void updateTest() {
		User user = new User();
		user.setId(id);
		user.setUsername("livk https");
		int result = userMapper.updateById(user);
		assertEquals(1, result);
	}

	@Order(3)
	@Test
	public void selectByIdTest() {
		User result = userMapper.selectById(id);
		assertNotNull(result);
	}

	@Order(4)
	@Test
	public void selectAllTest() {
		try (Page<User> page = PageHelper.<User>startPage(1, 10)
			.countColumn(FieldFunc.getName(User::getId))
			.doSelectPage(userMapper::list)) {
			PageInfo<User> result = new PageInfo<>(page);
			assertNotNull(result);
			String json = JsonMapperUtils.writeValueAsString(result);
			JavaType javaType = TypeFactoryUtils.javaType(PageInfo.class, User.class);
			PageInfo<User> customPage = JsonMapperUtils.readValue(json, javaType);
			assertNotNull(customPage);
		}
	}

	@Order(5)
	@Test
	public void deleteTest() {
		int result = userMapper.deleteById(id);
		assertEquals(1, result);
	}
}
