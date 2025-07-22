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

package com.livk.micrometer.tracing;

import com.livk.commons.micrometer.ContextSnapshots;
import com.livk.commons.micrometer.TraceEnvironmentPostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * {@link TraceEnvironmentPostProcessor}
 * </p>
 *
 * @author livk
 */
@Slf4j
@RestController
@SpringBootApplication
public class MicrometerTraceApp {

	public static void main(String[] args) {
		SpringApplication.run(MicrometerTraceApp.class, args);
	}

	@GetMapping("home")
	public String home() {
		log.info("home() has been called");
		ExecutorService wrap = ContextSnapshots.wrap(Executors.newFixedThreadPool(2, Thread.ofVirtual().factory()));
		for (int i = 0; i < 3; i++) {
			wrap.execute(() -> log.info("home"));
		}
		wrap.shutdown();
		return "Hello World!";
	}

}
