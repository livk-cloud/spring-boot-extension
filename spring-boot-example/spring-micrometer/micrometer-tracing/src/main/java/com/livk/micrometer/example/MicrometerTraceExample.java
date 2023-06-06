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

package com.livk.micrometer.example;

import com.livk.commons.spring.SpringLauncher;
import io.micrometer.context.ContextExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * {@link com.livk.commons.spring.TraceEnvironmentPostProcessor}
 * </p>
 *
 * @author livk
 */
@Slf4j
@RestController
@SpringBootApplication
public class MicrometerTraceExample {

	public static void main(String[] args) {
		SpringLauncher.run(args);
	}

	@GetMapping("home")
	public String home() {
		log.info("home() has been called");
		ExecutorService service = Executors.newFixedThreadPool(2);
		ExecutorService wrap = ContextExecutorService.wrap(service);
		for (int i = 0; i < 3; i++) {
			wrap.execute(() -> log.info("home"));
		}
		service.shutdown();
		return "Hello World!";
	}
}
