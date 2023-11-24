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

package com.livk.socket;

import com.livk.socket.session.WebSocketClientTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * WebSocketClientTestTest
 * </p>
 *
 * @author livk
 */
@Disabled("单独测试，性能损耗大")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
		useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class WebSocketClientTestTest {

	@Test
	void testMain() {
		String uri = "ws://127.0.0.1:8888/websocket/";
		int threadNum = 1000;
		ExecutorService service = Executors.newFixedThreadPool(threadNum);
		CountDownLatch countDownLatch = new CountDownLatch(threadNum);
		for (int i = 0; i < threadNum; i++) {
			String url = uri + "No" + i;
			service.submit(new WebSocketClientTest(url, countDownLatch));
			countDownLatch.countDown();
		}
		assertEquals(0, countDownLatch.getCount());
	}

}
