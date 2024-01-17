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

package com.livk.commons.expression;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author livk
 */
class ContextFactoryTest {

	private final static Map<String, String> map = Map.of("username", "livk");

	private final Method method = ParseMethodTest.class.getDeclaredMethod("parseMethod", String.class);

	ContextFactory contextFactory = new DefaultContextFactory();

	ContextFactoryTest() throws NoSuchMethodException {
	}

	@Test
	void create() {
		Context context = contextFactory.create(method, new String[] { "livk" });
		assertEquals(map.size(), context.size());
		assertEquals(map.keySet(), context.keySet());
		assertEquals(map.entrySet(), context.entrySet());
	}

	@Test
	void merge() {
		Context context = contextFactory.merge(method, new String[] { "root" }, map);
		assertEquals(1, context.size());
		assertEquals(map.keySet(), context.keySet());
		assertNotEquals(map.entrySet(), context.entrySet());
		assertTrue(context.containsKey("username"));
		assertTrue(context.containsValue("root"));
		assertFalse(context.containsValue("livk"));
		assertEquals("root", context.get("username"));
	}

}
