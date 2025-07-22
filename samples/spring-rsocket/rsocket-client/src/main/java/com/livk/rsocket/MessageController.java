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
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.service.RSocketServiceProxyFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.stream.Collectors;

/**
 * @author livk
 */
@Slf4j
@RestController
@RequestMapping("exchange")
public class MessageController {

	private final MessageService messageService;

	public MessageController(RSocketRequester.Builder rsocketRequesterBuilder, RSocketStrategies strategies) {
		RSocketRequester rsocketRequester = rsocketRequesterBuilder.rsocketStrategies(strategies)
			.tcp("localhost", 7000);
		this.messageService = RSocketServiceProxyFactory.builder(rsocketRequester)
			.build()
			.createClient(MessageService.class);
	}

	@GetMapping("request-response")
	public Mono<Message> requestResponse() {
		return messageService.requestResponse(new Message("客户端", "服务器"));
	}

	@GetMapping("fire-and-forget")
	public Mono<String> fireAndForget() {
		return messageService.fireAndForget(new Message("客户端", "服务器")).flatMap(unused -> Mono.just("fire and forget"));
	}

	@GetMapping("stream")
	public Mono<String> stream() {
		return messageService.stream(new Message("客户端", "服务器"))
			.collectList()
			.map(messages -> messages.stream().map(Message::toString).collect(Collectors.toList()))
			.map(list -> String.join(",", list));
	}

	@GetMapping("channel")
	public Mono<String> channel() {
		Mono<Duration> setting1 = Mono.just(Duration.ofSeconds(1));
		Mono<Duration> setting2 = Mono.just(Duration.ofSeconds(3)).delayElement(Duration.ofSeconds(5));
		Mono<Duration> setting3 = Mono.just(Duration.ofSeconds(5)).delayElement(Duration.ofSeconds(15));
		Flux<Duration> settings = Flux.concat(setting1, setting2, setting3);
		return messageService.channel(settings)
			.collectList()
			.map(messages -> messages.stream().map(Message::toString).collect(Collectors.toList()))
			.map(list -> String.join(",", list));
	}

}
