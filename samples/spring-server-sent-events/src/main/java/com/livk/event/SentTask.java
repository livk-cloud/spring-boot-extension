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

package com.livk.event;

import com.livk.commons.util.DateUtils;
import com.livk.event.context.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author livk
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SentTask {

	private final SseEmitterRepository<String> sseEmitterRepository;

	@Scheduled(cron = "0/10 * * * * ?")
	public void push() {
		for (Map.Entry<String, SseEmitter> sseEmitterEntry : sseEmitterRepository.all().entrySet()) {
			SseEmitter sseEmitter = sseEmitterEntry.getValue();
			try {
				sseEmitter.send(SseEmitter.event()
					.data(DateUtils.format(LocalDateTime.now(), DateUtils.YMD_HMS))
					.id(sseEmitterEntry.getKey()));
			}
			catch (Exception e) {
				log.error("推送异常:{}", e.getMessage());
				sseEmitter.complete();
				sseEmitterRepository.remove(sseEmitterEntry.getKey());
			}
		}
	}

}
