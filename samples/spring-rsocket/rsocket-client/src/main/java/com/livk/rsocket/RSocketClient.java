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
import io.rsocket.RSocket;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author livk
 */
@Slf4j
@RestController
public class RSocketClient {

	private final RSocketRequester rsocketRequester;

	public RSocketClient(RSocketRequester.Builder rsocketRequesterBuilder, RSocketStrategies strategies) {
		this.rsocketRequester = rsocketRequesterBuilder.rsocketStrategies(strategies).tcp("localhost", 7000);
	}

	@PreDestroy
	void shutdown() {
		RSocket rsocket = rsocketRequester.rsocket();
		if (rsocket != null) {
			rsocket.dispose();
		}
	}

	@GetMapping("request-response")
	public Mono<Message> requestResponse() {
		return this.rsocketRequester.route("request-response")
			.data(new Message("客户端", "服务器"))
			.retrieveMono(Message.class)
			.log();
	}

	@GetMapping("fire-and-forget")
	public Mono<String> fireAndForget() {
		return this.rsocketRequester.route("fire-and-forget")
			.data(new Message("客户端", "服务器"))
			.send()
			.then(Mono.defer(() -> Mono.just("fire and forget")));
	}

	@GetMapping("stream")
	public Mono<String> stream() {
		return Mono.just("stream");
	}

	@GetMapping("channel")
	public Mono<String> channel() {
		Mono<Duration> setting1 = Mono.just(Duration.ofSeconds(1));
		Mono<Duration> setting2 = Mono.just(Duration.ofSeconds(3)).delayElement(Duration.ofSeconds(5));
		Mono<Duration> setting3 = Mono.just(Duration.ofSeconds(5)).delayElement(Duration.ofSeconds(15));
		Flux<Duration> settings = Flux.concat(setting1, setting2, setting3)
			.doOnNext(d -> log.info("客户端channel发送消息 {}", d.getSeconds()));
		return settings.collectList().flatMap(durations -> Mono.just("channel"));
	}

}
