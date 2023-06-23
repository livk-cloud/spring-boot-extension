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

package com.livk.mybatisplugins.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.livk.commons.bean.BeanLambdaFunc;
import com.livk.commons.bean.domain.PageInfo;
import com.livk.mybatisplugins.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * UserServiceTest
 * </p>
 *
 * @author livk
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class UserServiceTest {

	@Autowired
	UserService userService;

	Integer id = 10;

	@Order(3)
	@Test
	void getById() {
		User result = userService.getById(id);
		assertNotNull(result);
	}

	@Order(2)
	@Test
	void updateById() {
		User user = new User();
		user.setId(id);
		user.setUsername("livk https");
		boolean result = userService.updateById(user);
		assertTrue(result);
	}

	@Order(1)
	@Test
	void save() {
		User user = new User();
		user.setId(id);
		user.setUsername("livk");
		boolean result = userService.save(user);
		assertTrue(result);
	}

	@Order(5)
	@Test
	void deleteById() {
		boolean result = userService.deleteById(id);
		assertTrue(result);
	}

	@Order(4)
	@Test
	void list() {
		try (Page<User> page = PageHelper.<User>startPage(1, 10)
			.countColumn(BeanLambdaFunc.fieldName(User::getId))
			.doSelectPage(userService::list)) {
			PageInfo<User> result = new PageInfo<>(page);
			assertNotNull(result);
		}
	}
}
