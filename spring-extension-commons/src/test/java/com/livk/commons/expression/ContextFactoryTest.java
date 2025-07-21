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

package com.livk.commons.expression;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class ContextFactoryTest {

	private final static Map<String, String> map = Map.of("username", "livk");

	private final Method method = ParseMethod.class.getDeclaredMethod("parseMethod", String.class);

	final ContextFactory contextFactory = new DefaultContextFactory();

	ContextFactoryTest() throws NoSuchMethodException {
	}

	@Test
	void create() {
		{
			Context context = contextFactory.create(method, new String[] { "livk" });
			assertEquals(map.size(), context.asMap().size());
			assertEquals(map.keySet(), context.asMap().keySet());
			assertEquals(map.entrySet(), context.asMap().entrySet());
		}
		{
			HashMap<String, Object> contextMap = Maps
				.newHashMap(contextFactory.create(method, new String[] { "root" }).asMap());
			contextMap.putAll(map);
			Context context = Context.create(contextMap);
			assertEquals(1, context.asMap().size());
			assertEquals(contextMap.keySet(), context.asMap().keySet());
			assertEquals(contextMap.entrySet(), context.asMap().entrySet());
			assertTrue(context.asMap().containsKey("username"));
			assertFalse(context.asMap().containsValue("root"));
			assertTrue(context.asMap().containsValue("livk"));
			assertEquals("livk", context.asMap().get("username"));
		}
	}

}
