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

package com.livk.spring;

import com.livk.commons.http.annotation.EnableHttpClient;
import com.livk.commons.http.annotation.HttpClientType;
import com.livk.commons.SpringLauncher;
import com.livk.selector.LivkImport;
import com.livk.starter01.EnableLivk;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * App
 * </p>
 *
 * @author livk
 */
@LivkImport
@EnableLivk
@EnableHttpClient({HttpClientType.REST_TEMPLATE, HttpClientType.WEB_CLIENT})
@SpringBootApplication
public class App {

	public static void main(String[] args) {
		System.setProperty("server.port", "9099");
		SpringLauncher.run(args);
	}

}
