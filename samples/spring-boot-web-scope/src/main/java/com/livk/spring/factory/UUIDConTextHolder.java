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

package com.livk.spring.factory;

import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * @author livk
 */
@UtilityClass
public class UUIDConTextHolder {

	private static final ThreadLocal<UUID> UUID_CONTEXT = new ThreadLocal<>();

	public UUID get() {
		return UUID_CONTEXT.get();
	}

	public void set() {
		UUID_CONTEXT.set(UUID.randomUUID());
	}

	public void remove() {
		UUID_CONTEXT.remove();
	}

}
