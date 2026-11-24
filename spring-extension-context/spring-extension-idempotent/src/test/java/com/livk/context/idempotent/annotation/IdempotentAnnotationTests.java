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

package com.livk.context.idempotent.annotation;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class IdempotentAnnotationTests {

	@Test
	void defaults() throws Exception {
		assertThat(Idempotent.class.getDeclaredMethod("ttl").getDefaultValue()).isEqualTo(60L);
		assertThat(Idempotent.class.getDeclaredMethod("ttlUnit").getDefaultValue()).isEqualTo(TimeUnit.SECONDS);
		assertThat(Idempotent.class.getDeclaredMethod("releaseOnError").getDefaultValue()).isEqualTo(true);
	}

}
