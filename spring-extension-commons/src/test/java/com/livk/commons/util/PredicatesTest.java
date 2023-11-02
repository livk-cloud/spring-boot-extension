/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.commons.util;

import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class PredicatesTest {

	@Test
	void allChecked() {
		assertFalse(Predicates.create("1", "").allChecked(StringUtils::hasText));
		assertTrue(Predicates.create("1", "2").allChecked(StringUtils::hasText));
	}

	@Test
	void anyChecked() {
		assertTrue(Predicates.create("1", "").anyChecked(StringUtils::hasText));
		assertFalse(Predicates.create(" ", " ").anyChecked(StringUtils::hasText));
	}

	@Test
	void noneMatch() {
		assertFalse(Predicates.create("1", "").noneMatch(StringUtils::hasText));
		assertTrue(Predicates.create(" ", " ").noneMatch(StringUtils::hasText));
	}
}
