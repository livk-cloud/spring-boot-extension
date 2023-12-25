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

package com.livk.commons.cglib;

import java.util.List;
import java.util.Set;

import com.livk.commons.beans.BeanLambdaFunc;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class BeanMapTest {

	@Test
	public void test() {
		List<Bean> list = List.of(new Bean().setId(1L).setUsername("root"), new Bean().setId(2L).setUsername("root"),
				new Bean().setId(3L).setUsername("root"));
		Bean bean = new Bean().setId(0L).setUsername("livk").setBeans(list);

		BeanMap beanMap = BeanMap.create(bean);
		assertEquals(0L, beanMap.get(BeanLambdaFunc.fieldName(Bean::getId)));
		assertEquals("livk", beanMap.get(BeanLambdaFunc.fieldName(Bean::getUsername)));
		assertEquals(Set.of(list), Set.of(beanMap.get(BeanLambdaFunc.fieldName(Bean::getBeans))));

		assertEquals(1L, beanMap.get(list.getFirst(), BeanLambdaFunc.fieldName(Bean::getId)));

		assertEquals(Long.class, beanMap.getPropertyType(BeanLambdaFunc.fieldName(Bean::getId)));
		assertEquals(String.class, beanMap.getPropertyType(BeanLambdaFunc.fieldName(Bean::getUsername)));
		assertEquals(List.class, beanMap.getPropertyType(BeanLambdaFunc.fieldName(Bean::getBeans)));
	}

	@Data
	@Accessors(chain = true)
	static class Bean {

		private Long id;

		private String username;

		private List<Bean> beans;

	}

}
