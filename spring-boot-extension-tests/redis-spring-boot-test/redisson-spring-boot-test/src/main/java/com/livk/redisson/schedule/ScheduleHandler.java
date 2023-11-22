/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.redisson.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RScheduledFuture;
import org.redisson.api.RedissonClient;
import org.redisson.api.WorkerOptions;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * ScheduleHandler
 * </p>
 *
 * @author livk
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleHandler {

	private final RedissonClient redissonClient;

	public void start() {
		RScheduledExecutorService executorService = redissonClient.getExecutorService("livk");
		executorService.registerWorkers(WorkerOptions.defaults().workers(12));
		RScheduledFuture<?> future = executorService.scheduleAtFixedRate((Runnable & Serializable) () -> {
			log.info("time:{}", System.currentTimeMillis());
			try {
				Thread.sleep(100L);
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}, 0, 1, TimeUnit.SECONDS);
		log.info("TaskId:{}", future.getTaskId());
	}

}
