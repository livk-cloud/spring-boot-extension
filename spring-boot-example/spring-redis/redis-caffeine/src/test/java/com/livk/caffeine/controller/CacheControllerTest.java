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

package com.livk.caffeine.controller;

import com.livk.autoconfigure.redis.supprot.RedisOps;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * CacheControllerTest
 * </p>
 *
 * @author livk
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class CacheControllerTest {
	@Autowired
	MockMvc mockMvc;

	@Autowired
    RedisOps redisOps;


	@Test
	void testGet() throws Exception {
		Set<String> result = new HashSet<>();
		String uuid = mockMvc.perform(get("/cache"))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn().getResponse().getContentAsString();
		result.add(uuid);
		for (int i = 0; i < 3; i++) {
			String newUUID = mockMvc.perform(get("/cache"))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().string(uuid))
				.andReturn().getResponse().getContentAsString();
			result.add(newUUID);
		}
		assertEquals(result.size(), 1);
	}

	@Test
	void testPut() throws Exception {
		Set<String> result = new HashSet<>();
		for (int i = 0; i < 3; i++) {
			String uuid = mockMvc.perform(post("/cache"))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
			result.add(uuid);
			String newUUID = mockMvc.perform(get("/cache"))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().string(uuid))
				.andReturn().getResponse().getContentAsString();
			result.add(newUUID);
		}
		assertEquals(result.size(), 3);
	}

	@Test
	void testDelete() throws Exception {
		mockMvc.perform(delete("/cache"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(content().string("over"));
	}

	@Test
	public void test() {
		ScanOptions options = ScanOptions.scanOptions().match("*").count(100).build();
		try (Cursor<String> cursor = redisOps.scan(options)) {
			while (cursor.hasNext()) {
				log.info("key:{} cursorId:{} position:{}", cursor.next(), cursor.getCursorId(), cursor.getPosition());
			}
		}

		try (Cursor<String> scan = redisOps.scan(options)) {
			Set<String> keys = scan.stream().limit(1).collect(Collectors.toSet());
			log.info("keys:{}", keys);
			assertEquals(1, keys.size());
		}
	}
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
