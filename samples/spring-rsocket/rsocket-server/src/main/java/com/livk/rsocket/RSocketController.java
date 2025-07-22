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

package com.livk.rsocket;

import com.livk.rsocket.common.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @author livk
 */
@Slf4j
@Controller
public class RSocketController {

	@MessageMapping("request-response")
	public Message requestResponse(Message request) {
		log.info("{}", request);
		return new Message("server", "client");
	}

	@MessageMapping("fire-and-forget")
	public void fireAndForget(Message request) {
		log.info("收到fire-and-forget请求: {}", request);
	}

	@MessageMapping("stream")
	Flux<Message> stream(Message request) {
		log.info("收到流式请求: {}", request);
		return Flux.interval(Duration.ofSeconds(1)).map(index -> new Message("服务端", "客户端", index)).log();
	}

	@MessageMapping("channel")
	Flux<Message> channel(final Flux<Duration> settings) {
		return settings.doOnNext(setting -> log.info("发射间隔为 {} 秒.", setting.getSeconds()))
			.switchMap(setting -> Flux.interval(setting).map(index -> new Message("服务端", "客户端", index)))
			.log();
	}

}
