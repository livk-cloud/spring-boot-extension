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

package com.livk.admin.server.config;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * <p>
 * CustomNotifier
 * </p>
 *
 * @author livk
 */
@Slf4j
@Component
public class CustomNotifier extends AbstractEventNotifier {

	protected CustomNotifier(InstanceRepository repository) {
		super(repository);
	}

	@NonNull
	@Override
	protected Mono<Void> doNotify(@NonNull InstanceEvent event, @NonNull Instance instance) {
		return Mono.fromRunnable(() -> {
			if (event instanceof InstanceStatusChangedEvent statusChangedEvent) {
				log.info("Instance {} ({}) is {}", instance.getRegistration().getName(), event.getInstance(),
						statusChangedEvent.getStatusInfo().getStatus());
			}
			else {
				log.info("Instance {} ({}) {}", instance.getRegistration().getName(), event.getInstance(),
						event.getType());
			}
		});
	}

}
