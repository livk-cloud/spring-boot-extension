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

package com.livk.commons.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class BeanConverterTests {

	@Test
	void toMap() {
		TargetBean source = new TargetBean("source", 10);
		Map<String, Object> map = BeanConverter.toMap(source);
		assertThat(map).containsEntry("beanName", "source")
			.containsEntry("beanNo", 10)
			.containsEntry("class", TargetBean.class);
	}

	@Test
	void toMapIsUnmodifiable() {
		Map<String, Object> map = BeanConverter.toMap(new TargetBean("x", 1));
		assertThatThrownBy(() -> map.put("new", "value")).isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	void fromMap() {
		TargetBean source = new TargetBean("source", 10);
		Map<String, Object> map = Map.of("beanName", "source", "beanNo", 10);
		TargetBean target = BeanConverter.fromMap(map, TargetBean.class);
		assertThat(target).isEqualTo(source);
	}

	@Test
	void fromMapWithNoArgConstructorMissingThrows() {
		Map<String, Object> map = Map.of("beanName", "source", "beanNo", 10);
		assertThatThrownBy(() -> BeanConverter.fromMap(map, SourceBean.class))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Missing no-argument constructor");
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
