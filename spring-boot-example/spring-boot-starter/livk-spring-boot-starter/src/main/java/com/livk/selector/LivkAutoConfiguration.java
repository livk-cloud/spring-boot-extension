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

package com.livk.selector;

import com.livk.auto.service.annotation.SpringAutoService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * <p>
 * LivkAutoConfiguration
 * </p>
 *
 * @author livk
 */
@Slf4j
@AutoConfiguration
@SpringAutoService(LivkImport.class)
public class LivkAutoConfiguration {

	@PostConstruct
	public void init() {
		log.info("{} is init", LivkAutoConfiguration.class);
	}

}
