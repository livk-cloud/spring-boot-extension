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

package com.livk.http.service;

import com.livk.autoconfigure.http.factory.AdapterType;
import com.livk.autoconfigure.http.annotation.HttpProvider;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Map;

/**
 * <p>
 * RemoteService
 * </p>
 *
 * @author livk
 */
@HttpProvider(type = AdapterType.WEB_CLIENT, url = "http://localhost:${server.port:8080}/rpc")
public interface SpringBootService {

	@GetExchange("spring-boot")
	Map<String, String> springBoot();

}
