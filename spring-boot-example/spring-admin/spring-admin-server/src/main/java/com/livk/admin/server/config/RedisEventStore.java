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

package com.livk.admin.server.config;

import com.livk.autoconfigure.redis.supprot.ReactiveRedisOps;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.values.InstanceId;
import de.codecentric.boot.admin.server.eventstore.ConcurrentMapEventStore;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * RedisEventStore
 * </p>
 *
 * @author livk
 */
@Component
public class RedisEventStore extends ConcurrentMapEventStore {

	private static final String INSTANCE_EVENT_KEY = "Event";

	private final ReactiveHashOperations<String, String, List<InstanceEvent>> hashOperations;

	public RedisEventStore(ReactiveRedisOps reactiveRedisOps) {
		super(100, new ConcurrentHashMap<>());
		hashOperations = reactiveRedisOps.opsForHash();
	}

	@NonNull
	@Override
	public Mono<Void> append(List<InstanceEvent> events) {
		if (events.isEmpty()) {
			return Mono.empty();
		}
		InstanceId id = events.get(0).getInstance();
		if (!events.stream().map(InstanceEvent::getInstance).allMatch(id::equals)) {
			throw new IllegalArgumentException("events must only refer to the same instance.");
		}
		return hashOperations.put(INSTANCE_EVENT_KEY, id.getValue(), events)
			.flatMap(bool -> super.append(events).then(Mono.fromRunnable(() -> this.publish(events))));
	}

}
