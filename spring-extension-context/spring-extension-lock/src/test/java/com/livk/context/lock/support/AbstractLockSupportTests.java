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

package com.livk.context.lock.support;

import com.livk.context.lock.LockType;
import com.livk.context.lock.exception.LockException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractLockSupportTests {

	@Test
	void tryLockPassesLeaseTimeBeforeWaitTime() {
		TestLockSupport support = new TestLockSupport();

		assertThat(support.tryLock(LockType.LOCK, "key", 10, 20, false)).isTrue();

		assertThat(support.leaseTime).isEqualTo(10);
		assertThat(support.waitTime).isEqualTo(20);
	}

	private static final class TestLockSupport extends AbstractLockSupport<String> {

		private long leaseTime;

		private long waitTime;

		@Override
		protected String getLock(LockType type, String key) {
			return key;
		}

		@Override
		protected boolean unlock(String lock) {
			return true;
		}

		@Override
		protected boolean tryLock(String lock, long leaseTime, long waitTime) throws LockException {
			this.leaseTime = leaseTime;
			this.waitTime = waitTime;
			return true;
		}

		@Override
		protected void lock(String lock) throws LockException {
		}

		@Override
		protected boolean isLocked(String lock) {
			return true;
		}

	}

}
