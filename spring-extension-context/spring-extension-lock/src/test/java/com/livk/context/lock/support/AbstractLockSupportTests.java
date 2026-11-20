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
import com.livk.context.lock.exception.UnSupportLockException;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

	@Test
	void rejectsBlankKeyAndNullType() {
		TestLockSupport support = new TestLockSupport();

		assertThatIllegalArgumentException().isThrownBy(() -> support.lock("")).withMessageContaining("Lock key");
		assertThatIllegalArgumentException().isThrownBy(() -> support.lock(" ", LockType.LOCK))
			.withMessageContaining("Lock key");
		assertThatIllegalArgumentException().isThrownBy(() -> support.lock("key", null))
			.withMessageContaining("Lock type");
	}

	@Test
	void asyncFallsBackToSyncWhenNotSupported() {
		TestLockSupport support = new TestLockSupport();

		assertThat(support.lock("key").async().leaseTime(4).waitTime(6).tryLock()).isTrue();
		assertThat(support.leaseTime).isEqualTo(4);
		assertThat(support.waitTime).isEqualTo(6);
		support.lock("key").unlock();
	}

	@Test
	void defaultAsyncHooksThrowWhenInvokedDirectly() {
		TestLockSupport support = new TestLockSupport();

		assertThatExceptionOfType(UnSupportLockException.class).isThrownBy(() -> support.tryLockAsync("x", 1, 1))
			.withMessageContaining("isn't support");
		assertThatExceptionOfType(UnSupportLockException.class).isThrownBy(() -> support.doLockAsync("x", 1))
			.withMessageContaining("isn't support");
	}

	@Test
	void asyncDelegatesWhenSupported() {
		AsyncTestLockSupport support = new AsyncTestLockSupport();

		assertThat(support.lock("async").async().leaseTime(5).waitTime(7).tryLock()).isTrue();
		assertThat(support.asyncTryCalls.get()).isEqualTo(1);
		assertThat(support.leaseTime).isEqualTo(5);
		assertThat(support.waitTime).isEqualTo(7);
		support.lock("async").unlock();

		support.lock("async-block").async().leaseTime(9).lock();
		assertThat(support.asyncLockCalls.get()).isEqualTo(1);
		assertThat(support.leaseTime).isEqualTo(9);
		support.lock("async-block").unlock();
	}

	@Test
	void sameKeyIsReentrantUntilFullyUnlocked() {
		TestLockSupport support = new TestLockSupport();
		DistLockFactory.SpecLock first = support.lock("reenter");
		DistLockFactory.SpecLock second = support.lock("reenter");

		first.lock();
		second.lock();
		assertThat(support.isHeld("reenter")).isTrue();
		assertThat(support.holdCount("reenter")).isEqualTo(2);

		first.unlock();
		assertThat(support.isHeld("reenter")).isTrue();
		assertThat(support.holdCount("reenter")).isEqualTo(1);

		second.unlock();
		assertThat(support.isHeld("reenter")).isFalse();
	}

	@Test
	void unlockWithoutHoldIsNoOp() {
		TestLockSupport support = new TestLockSupport();
		support.lock("missing").unlock();
		assertThat(support.isHeld("missing")).isFalse();
	}

	private static class TestLockSupport extends AbstractLockSupport<String> {

		protected long leaseTime;

		protected long waitTime;

		private LockType usedType;

		private final Map<String, Integer> held = new ConcurrentHashMap<>();

		boolean isHeld(String key) {
			return this.held.containsKey(key);
		}

		int holdCount(String key) {
			return this.held.getOrDefault(key, 0);
		}

		@Override
		protected String getLock(LockType type, String key) {
			this.usedType = type;
			return key;
		}

		@Override
		protected boolean unlock(String lock) {
			Integer count = this.held.get(lock);
			if (count == null) {
				return false;
			}
			if (count <= 1) {
				this.held.remove(lock);
			}
			else {
				this.held.put(lock, count - 1);
			}
			return true;
		}

		@Override
		protected boolean tryLock(String lock, long leaseTime, long waitTime) throws LockException {
			this.leaseTime = leaseTime;
			this.waitTime = waitTime;
			this.held.merge(lock, 1, Integer::sum);
			return true;
		}

		@Override
		protected void doLock(String lock, long leaseTime) throws LockException {
			this.leaseTime = leaseTime;
			this.held.merge(lock, 1, Integer::sum);
		}

		@Override
		protected boolean isLocked(String lock) {
			return this.held.containsKey(lock);
		}

		// expose protected defaults for direct assertion
		@Override
		protected boolean tryLockAsync(String lock, long leaseTime, long waitTime) throws LockException {
			return super.tryLockAsync(lock, leaseTime, waitTime);
		}

		@Override
		protected void doLockAsync(String lock, long leaseTime) throws LockException {
			super.doLockAsync(lock, leaseTime);
		}

	}

	private static final class AsyncTestLockSupport extends TestLockSupport {

		private final AtomicInteger asyncTryCalls = new AtomicInteger();

		private final AtomicInteger asyncLockCalls = new AtomicInteger();

		@Override
		protected boolean supportAsync() {
			return true;
		}

		@Override
		protected boolean tryLockAsync(String lock, long leaseTime, long waitTime) {
			this.asyncTryCalls.incrementAndGet();
			return tryLock(lock, leaseTime, waitTime);
		}

		@Override
		protected void doLockAsync(String lock, long leaseTime) {
			this.asyncLockCalls.incrementAndGet();
			doLock(lock, leaseTime);
		}

	}

}
