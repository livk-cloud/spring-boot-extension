/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.redisson.lock;

import com.livk.testcontainers.RedisContainer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class ShopControllerTest {

	@Container
	@ServiceConnection
	static RedisContainer redis = new RedisContainer().withPassword("123456");

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.redisson.config.single-server-config.address",
				() -> "redis://" + redis.getHost() + ":" + redis.getMappedPort(6379));
	}

	@Autowired
	MockMvc mockMvc;

	@Order(1)
	@Test
	void testBuyLocal() throws InterruptedException {
		try (ExecutorService service = Executors.newFixedThreadPool(10, Thread.ofVirtual().factory())) {
			CountDownLatch countDownLatch = new CountDownLatch(10);
			for (int i = 0; i < 10; i++) {
				service.submit(() -> {
					try {
						mockMvc.perform(post("/shop/buy/distributed")).andExpect(status().isOk());
						countDownLatch.countDown();
					}
					catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
			}
			countDownLatch.await();
			service.shutdown();
		}
	}

	@Order(2)
	@Test
	void testResult() throws Exception {
		mockMvc.perform(get("/shop/result"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("redisson.buyCount", 10).exists())
			.andExpect(jsonPath("redisson.buySucCount", 10).exists())
			.andExpect(jsonPath("redisson.num", 480).exists());
	}

}

// Generated with love by TestMe :) Please report issues and submit feature requests at:
// http://weirddev.com/forum#!/testme
