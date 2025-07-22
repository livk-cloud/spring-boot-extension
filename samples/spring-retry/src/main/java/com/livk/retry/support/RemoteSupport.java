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

package com.livk.retry.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author livk
 */
@Slf4j
@Component
public class RemoteSupport {

	@Retryable(retryFor = IllegalArgumentException.class, backoff = @Backoff(delay = 2000, multiplier = 1.5))
	public String call(String username) {
		if ("fail".equals(username)) {
			log.info("Remote Fail time:{}", LocalDateTime.now());
			throw new IllegalArgumentException("Fail RPC");
		}
		return "SUC";
	}

	@Recover
	public String recover(IllegalArgumentException e) {
		log.error("remote exception msg:{}", e.getMessage());
		return "recover SUC";
	}

}
