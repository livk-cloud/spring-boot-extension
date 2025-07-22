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

package com.livk.zookeeper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.Lock;

/**
 * @author livk
 */
@Slf4j
@RequestMapping("lock")
@RestController
@RequiredArgsConstructor
public class LockController {

	private final LockRegistry lockRegistry;

	@GetMapping
	public void lock(@RequestParam Integer id) throws InterruptedException {
		Lock lock = lockRegistry.obtain("zookeeper");
		while (!lock.tryLock()) {
			// 获取不到锁直接空转
			Thread.sleep(100);
		}
		try {
			log.info("{} is locked", id);
			Thread.sleep(1000);
		}
		catch (InterruptedException e) {
			log.warn("interrupted", e);
			Thread.currentThread().interrupt();
		}
		finally {
			lock.unlock();
		}
	}

}
