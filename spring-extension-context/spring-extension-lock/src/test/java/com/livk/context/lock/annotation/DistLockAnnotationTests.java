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

package com.livk.context.lock.annotation;

import com.livk.context.lock.LockType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class DistLockAnnotationTests {

	@Test
	void defaults() throws Exception {
		assertThat(DistLock.class.getDeclaredMethod("type").getDefaultValue()).isEqualTo(LockType.LOCK);
		assertThat(DistLock.class.getDeclaredMethod("leaseTime").getDefaultValue()).isEqualTo(-1L);
		assertThat(DistLock.class.getDeclaredMethod("waitTime").getDefaultValue()).isEqualTo(3L);
		assertThat(DistLock.class.getDeclaredMethod("async").getDefaultValue()).isEqualTo(false);
	}

}
