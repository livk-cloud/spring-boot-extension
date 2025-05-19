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

package com.livk.commons.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

/**
 * <p>
 * BeanUtilsTest
 * </p>
 *
 * @author livk
 */
class BeanUtilsTest {

	static SourceBean bean = new SourceBean("source", 10);

	static List<SourceBean> beanList = List.of(new SourceBean("source", 10), new SourceBean("target", 9));

	@Test
	void copy() {
		TargetBean result = BeanUtils.copy(bean, TargetBean.class);
		TargetBean targetBean = new TargetBean("source", 10);
		assertEquals(result, targetBean);
	}

	@Test
	void copySupplier() {
		TargetBean result = BeanUtils.copy(bean, TargetBean::new);
		TargetBean targetBean = new TargetBean("source", 10);
		assertEquals(result, targetBean);
	}

	@Test
	void copyList() {
		List<TargetBean> result = BeanUtils.copyList(beanList, TargetBean.class);
		List<TargetBean> targetBeans = List.of(new TargetBean("source", 10), new TargetBean("target", 9));
		assertEquals(result, targetBeans);
	}

	@Test
	void convert() {
		{
			TargetBean source = new TargetBean("source", 10);
			Map<String, Object> convert = BeanUtils.convert(source);
			assertEquals("source", convert.get("beanName"));
			assertEquals(10, convert.get("beanNo"));
			assertEquals(TargetBean.class, convert.get("class"));

			TargetBean target = BeanUtils.convert(convert);
			assertEquals(source, target);
		}

		{
			SourceBean source = new SourceBean("source", 10);
			Map<String, Object> convert = BeanUtils.convert(source);
			assertEquals("source", convert.get("beanName"));
			assertEquals(10, convert.get("beanNo"));
			assertEquals(SourceBean.class, convert.get("class"));

			assertThrowsExactly(IllegalArgumentException.class, () -> BeanUtils.convert(convert),
					"Missing no-argument constructor");
		}
	}

	record SourceBean(String beanName, Integer beanNo) {
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class TargetBean {

		private String beanName;

		private Integer beanNo;

	}

}
