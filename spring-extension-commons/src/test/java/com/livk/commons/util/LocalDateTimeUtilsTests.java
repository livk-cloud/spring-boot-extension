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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@Slf4j
class LocalDateTimeUtilsTests {

	@Test
	void timestampTest() {
		Long result = LocalDateTimeUtils.timestamp(LocalDateTime.now());
		assertThat(result).isNotNull();
	}

	@Test
	void localDateTimeTest() {
		LocalDateTime result = LocalDateTimeUtils.localDateTime(1663063303L);
		assertThat(result).isNotNull();
	}

	@Test
	void formatTest() {
		String result = LocalDateTimeUtils.format(LocalDateTime.now(),
				DateTimeFormatter.ofPattern(LocalDateTimeUtils.YMD_HMS));
		assertThat(result).isNotNull();
	}

	@Test
	void testFormatTest() {
		String result = LocalDateTimeUtils.format(LocalDateTime.now(), LocalDateTimeUtils.YMD_HMS);
		assertThat(result).isNotNull();
	}

	@Test
	void parseTest() {
		LocalDateTime result = LocalDateTimeUtils.parse("2022-09-13 18:05:12", LocalDateTimeUtils.YMD_HMS);
		assertThat(result).isNotNull();
	}

}
