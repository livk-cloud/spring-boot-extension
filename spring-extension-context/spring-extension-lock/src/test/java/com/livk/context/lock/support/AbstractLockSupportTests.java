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

import com.livk.context.lock.DistLockFactory;
import com.livk.context.lock.LockType;
import com.livk.context.lock.exception.LockException;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
	void unlockReleasesHeldLock() {
		TestLockSupport support = new TestLockSupport();
		DistLockFactory.SpecLock spec = support.lock("key");

		spec.lock();
		assertThat(support.isHeld("key")).isTrue();

		spec.unlock();
		assertThat(support.isHeld("key")).isFalse();
	}

	/**
	 * 同线程嵌套两把不同的锁时，各 SpecLock 应独立 unlock，互不影响。
	 */
	@Test
	void nestedLockUnlockShouldReleaseEachLockIndependently() {
		TestLockSupport support = new TestLockSupport();
		DistLockFactory.SpecLock outer = support.lock("outer");
		DistLockFactory.SpecLock inner = support.lock("inner");

		outer.lock();
		inner.lock();
		assertThat(support.isHeld("outer")).isTrue();
		assertThat(support.isHeld("inner")).isTrue();

		inner.unlock();
		assertThat(support.isHeld("inner")).isFalse();
		assertThat(support.isHeld("outer")).isTrue();

		outer.unlock();
		assertThat(support.isHeld("outer")).isFalse();
	}

	private static final class TestLockSupport extends AbstractLockSupport<String> {

		private long leaseTime;

		private long waitTime;

		private LockType usedType;

		private final Set<String> held = ConcurrentHashMap.newKeySet();

		boolean isHeld(String key) {
			return this.held.contains(key);
		}

		@Override
		protected String getLock(LockType type, String key) {
			this.usedType = type;
			return key;
		}

		@Override
		protected boolean unlock(String lock) {
			return this.held.remove(lock);
		}

		@Override
		protected boolean tryLock(String lock, long leaseTime, long waitTime) throws LockException {
			this.leaseTime = leaseTime;
			this.waitTime = waitTime;
			this.held.add(lock);
			return true;
		}

		@Override
		protected void doLock(String lock, long leaseTime) throws LockException {
			this.held.add(lock);
		}

		@Override
		protected boolean isLocked(String lock) {
			return this.held.contains(lock);
		}

	}

}
