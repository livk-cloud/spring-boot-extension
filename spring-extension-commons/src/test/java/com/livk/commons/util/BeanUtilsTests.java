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

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class BeanUtilsTests {

	static final SourceBean bean = new SourceBean("source", 10);

	// --- copy(Object, Class) ---

	@Test
	void copyWithClass() {
		TargetBean result = BeanUtils.copy(bean, TargetBean.class);
		assertThat(result).isEqualTo(new TargetBean("source", 10));
	}

	@Test
	void copyWithNullSourceReturnsEmptyTarget() {
		TargetBean result = BeanUtils.copy(null, TargetBean.class);
		assertThat(result).isNotNull();
		assertThat(result.getBeanName()).isNull();
		assertThat(result.getBeanNo()).isNull();
	}

	// --- copy(Object, Supplier) ---

	@Test
	void copyWithSupplier() {
		TargetBean result = BeanUtils.copy(bean, TargetBean::new);
		assertThat(result).isEqualTo(new TargetBean("source", 10));
	}

	@Test
	void copyWithNullSupplierReturnsNull() {
		TargetBean result = BeanUtils.copy(bean, (Supplier<TargetBean>) null);
		assertThat(result).isNull();
	}

	// --- copyList ---

	@Test
	void copyList() {
		List<SourceBean> sourceList = List.of(new SourceBean("source", 10), new SourceBean("target", 9));
		List<TargetBean> result = BeanUtils.copyList(sourceList, TargetBean.class);
		assertThat(result).containsExactly(new TargetBean("source", 10), new TargetBean("target", 9));
	}

	@Test
	void copyListWithEmptyListReturnsEmpty() {
		List<TargetBean> result = BeanUtils.copyList(List.of(), TargetBean.class);
		assertThat(result).isEmpty();
	}

	// --- convert(Object) -> Map ---

	@Test
	void convertBeanToMap() {
		TargetBean source = new TargetBean("source", 10);
		Map<String, Object> map = BeanUtils.convert(source);
		assertThat(map).containsEntry("beanName", "source")
			.containsEntry("beanNo", 10)
			.containsEntry("class", TargetBean.class);
	}

	@Test
	void convertBeanToMapIsUnmodifiable() {
		Map<String, Object> map = BeanUtils.convert(new TargetBean("x", 1));
		assertThatThrownBy(() -> map.put("new", "value")).isInstanceOf(UnsupportedOperationException.class);
	}

	// --- convert(Map) -> Bean ---

	@Test
	void convertMapToBean() {
		TargetBean source = new TargetBean("source", 10);
		Map<String, Object> map = BeanUtils.convert(source);
		TargetBean target = BeanUtils.convert(map);
		assertThat(target).isEqualTo(source);
	}

	@Test
	void convertMapWithoutClassKeyThrows() {
		Map<String, Object> map = Map.of("beanName", "source");
		assertThatThrownBy(() -> BeanUtils.convert(map)).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("class must not be null");
	}

	@Test
	void convertMapWithNoArgConstructorMissingThrows() {
		SourceBean source = new SourceBean("source", 10);
		Map<String, Object> map = BeanUtils.convert(source);
		assertThatThrownBy(() -> BeanUtils.convert(map)).isInstanceOf(IllegalArgumentException.class)
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
