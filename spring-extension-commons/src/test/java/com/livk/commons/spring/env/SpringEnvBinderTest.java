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

package com.livk.commons.spring.env;

import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

/**
 * @author livk
 */
@SpringBootTest({
	"com.livk.of.code = 200",
	"com.livk.of.msg = root",
	"com.livk.of.data = 666",
	"com.livk.of.int = 1",
	"com.livk.of.str = 1",

	"com.livk.list.int[0] = 1",
	"com.livk.list.int[1] = 1",
	"com.livk.list.str[0] = 1",
	"com.livk.list.str[1] = 1",

	"com.livk.set.int[0] = 1",
	"com.livk.set.int[1] = 2",
	"com.livk.set.str[0] = 1",
	"com.livk.set.str[1] = 2",

	"com.livk.map.int.a = 1",
	"com.livk.map.int.b = 2",
	"com.livk.map.str.a = 1",
	"com.livk.map.str.b = 2",

	"com.livk.pro.str.a = 1",
	"com.livk.pro.str.b = 2",

	"com.livk.bind.code = 200",
	"com.livk.bind.msg = root",
})
class SpringEnvBinderTest {

	final static String SUFFIX = "com.livk.";
	@Autowired
	Environment environment;
	SpringEnvBinder binder;

	@BeforeEach
	void init() {
		binder = new SpringEnvBinder(environment);
	}

	@Test
	void of() {
		assertEquals(1, binder.<Integer>of(SUFFIX + "of.int", ResolvableType.forClass(Integer.class)).get());
		assertEquals("1", binder.<String>of(SUFFIX + "of.str", ResolvableType.forClass(String.class)).get());

		EntityResult<String> result = new EntityResult<String>().setCode(200).setMsg("root").setData("666");
		ResolvableType resolvableType = ResolvableType.forClassWithGenerics(EntityResult.class, String.class);
		assertEquals(result, binder.<EntityResult<String>>of(SUFFIX + "of", resolvableType).get());
	}

	@Test
	void listOf() {
		assertIterableEquals(List.of(1, 1), binder.listOf(SUFFIX + "list.int", Integer.class).get());
		assertIterableEquals(List.of("1", "1"), binder.listOf(SUFFIX + "list.str", String.class).get());
	}

	@Test
	void setOf() {
		assertEquals(Set.of(1, 2), binder.setOf(SUFFIX + "set.int", Integer.class).get());
		assertEquals(Set.of("1", "2"), binder.setOf(SUFFIX + "set.str", String.class).get());
	}

	@Test
	void mapOf() {
		assertEquals(Map.of("a", 1, "b", 2), binder.mapOf(SUFFIX + "map.int", String.class, Integer.class).get());
		assertEquals(Map.of("a", "1", "b", "2"), binder.mapOf(SUFFIX + "map.str", String.class, String.class).get());
	}

	@Test
	void propertiesOf() {
		Properties properties = new Properties();
		properties.put("a", "1");
		properties.put("b", "2");
		assertEquals(properties, binder.propertiesOf(SUFFIX + "map.str").get());
	}

	@Test
	void bind() {
		Result result = new Result().setCode(200).setMsg("root");
		assertEquals(result, binder.bind(SUFFIX + "bind", Result.class).get());
	}

	@Data
	@Accessors(chain = true)
	private static class EntityResult<T> {
		private Integer code;

		private String msg;

		private T data;
	}

	@Data
	@Accessors(chain = true)
	private static class Result {
		private Integer code;

		private String msg;
	}
}
