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

package com.livk.commons.function;

import com.livk.commons.bean.domain.Pair;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class FieldFuncTest {

	final Field field1 = Maker.class.getDeclaredField("no");
	final Field field2 = Maker.class.getDeclaredField("username");

	FieldFuncTest() throws NoSuchFieldException {
	}

	@Test
	void get() {
		assertEquals("key", FieldFunc.<Pair<String, String>>getName(Pair::key));
		assertEquals("value", FieldFunc.<Pair<String, String>>getName(Pair::value));
		assertEquals(field1.getName(), FieldFunc.getName(Maker::getNo));
		assertEquals(field2.getName(), FieldFunc.getName(Maker::getUsername));
	}

	@Test
	void getField() {
		assertEquals(field1, FieldFunc.get(Maker::getNo));
		assertEquals(field2, FieldFunc.get(Maker::getUsername));
	}

	@Data
	static class Maker {
		private Integer no;

		private String username;
	}
}
