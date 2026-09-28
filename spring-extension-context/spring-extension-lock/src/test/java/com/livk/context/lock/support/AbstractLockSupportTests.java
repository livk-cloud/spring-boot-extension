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
	void tryLockPassesLeaseTimeAndWaitTime() {
		TestLockSupport support = new TestLockSupport();

		assertThat(support.lock("key").leaseTime(10).waitTime(20).tryLock()).isTrue();

		assertThat(support.leaseTime).isEqualTo(10);
		assertThat(support.waitTime).isEqualTo(20);
	}

	@Test
	void tryLockUsesDefaultValues() {
		TestLockSupport support = new TestLockSupport();

		assertThat(support.lock("key").tryLock()).isTrue();

		assertThat(support.leaseTime).isEqualTo(-1);
		assertThat(support.waitTime).isEqualTo(3);
	}

	@Test
	void lockSpecTypeCanBeConfigured() {
		TestLockSupport support = new TestLockSupport();

		assertThat(support.lock("key", LockType.FAIR).tryLock()).isTrue();

		assertThat(support.usedType).isEqualTo(LockType.FAIR);
	}

	@Test
	void unlockReleasesThreadLocal() {
		TestLockSupport support = new TestLockSupport();

		support.lock("key").lock();
		assertThat(support.locked).isTrue();

		support.lock("key").unlock();
		assertThat(support.locked).isFalse();
	}

	private static final class TestLockSupport extends AbstractLockSupport<String> {

		private long leaseTime;

		private long waitTime;

		private LockType usedType;

		private boolean locked = false;

		@Override
		protected String getLock(LockType type, String key) {
			this.usedType = type;
			return key;
		}

		@Override
		protected boolean unlock(String lock) {
			this.locked = false;
			return true;
		}

		@Override
		protected boolean tryLock(String lock, long leaseTime, long waitTime) throws LockException {
			this.leaseTime = leaseTime;
			this.waitTime = waitTime;
			this.locked = true;
			return true;
		}

		@Override
		protected void doLock(String lock, long leaseTime) throws LockException {
			this.locked = true;
		}

		@Override
		protected boolean isLocked(String lock) {
			return this.locked;
		}

	}

}
