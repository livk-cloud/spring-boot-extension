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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class ContextFactoryTests {

	private final Map<String, String> map = Map.of("username", "livk");

	private final Method method = ParseMethod.class.getDeclaredMethod("parseMethod", String.class);

	final ContextFactory contextFactory = new DefaultContextFactory();

	ContextFactoryTests() throws NoSuchMethodException {
	}

	@Test
	void create() {
		{
			Context context = contextFactory.create(method, new String[] { "livk" });
			assertThat(context.asMap()).hasSize(map.size());
			assertThat(context.asMap().keySet()).isEqualTo(map.keySet());
			assertThat(context.asMap().entrySet()).isEqualTo(map.entrySet());
		}
		{
			HashMap<String, Object> contextMap = Maps
				.newHashMap(contextFactory.create(method, new String[] { "root" }).asMap());
			contextMap.putAll(map);
			Context context = Context.create(contextMap);
			assertThat(context.asMap()).hasSize(1);
			assertThat(context.asMap().keySet()).isEqualTo(contextMap.keySet());
			assertThat(context.asMap().entrySet()).isEqualTo(contextMap.entrySet());
			assertThat(context.asMap()).containsKey("username");
			assertThat(context.asMap()).doesNotContainValue("root");
			assertThat(context.asMap()).containsValue("livk");
			assertThat(context.asMap().get("username")).isEqualTo("livk");
		}

	}

}
