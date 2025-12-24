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

package com.livk.context.mybatis.enums;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class InjectTypeTests {

	@Test
	@SuppressWarnings("deprecation")
	void test() {
		assertThat(InjectType.DEFAULT.handler()).isNull();
		assertThat(InjectType.DEFAULT.type()).isEqualTo(Object.class);

		assertThat(InjectType.DATE.handler()).isNotNull().isInstanceOf(Date.class);
		assertThat(InjectType.DATE.type()).isAssignableFrom(Date.class);

		assertThat(InjectType.LOCAL_DATE.handler()).isNotNull().isInstanceOf(LocalDate.class);
		assertThat(InjectType.LOCAL_DATE.type()).isAssignableFrom(LocalDate.class);

		assertThat(InjectType.LOCAL_DATE_TIME.handler()).isNotNull().isInstanceOf(LocalDateTime.class);
		assertThat(InjectType.LOCAL_DATE_TIME.type()).isAssignableFrom(LocalDateTime.class);

		assertThat(InjectType.LOCAL_TIME.handler()).isNotNull().isInstanceOf(LocalTime.class);
		assertThat(InjectType.LOCAL_TIME.type()).isAssignableFrom(LocalTime.class);

		assertThat(InjectType.TIMESTAMP.handler()).isNotNull().isInstanceOf(Long.class);
		assertThat(InjectType.TIMESTAMP.type()).isAssignableFrom(Long.class);
	}

	@Test
	void testHandler() {
		assertThat(InjectType.handler(Date.class)).isInstanceOf(Date.class);
		assertThat(InjectType.handler(LocalDate.class)).isInstanceOf(LocalDate.class);
		assertThat(InjectType.handler(LocalDateTime.class)).isInstanceOf(LocalDateTime.class);
		assertThat(InjectType.handler(LocalTime.class)).isInstanceOf(LocalTime.class);
		assertThat(InjectType.handler(Long.class)).isInstanceOf(Long.class);
		assertThat(InjectType.handler(String.class)).isNull();
	}

}
