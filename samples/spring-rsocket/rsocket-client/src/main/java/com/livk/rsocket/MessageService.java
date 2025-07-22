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
import org.springframework.messaging.rsocket.service.RSocketExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author livk
 */
public interface MessageService {

	@RSocketExchange("request-response")
	Mono<Message> requestResponse(Message request);

	@RSocketExchange("fire-and-forget")
	Mono<Void> fireAndForget(Message request);

	@RSocketExchange("stream")
	Flux<Message> stream(Message request);

	@RSocketExchange("channel")
	Flux<Message> channel(Flux<Duration> settings);

}
