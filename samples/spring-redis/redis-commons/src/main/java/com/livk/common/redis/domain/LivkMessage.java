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

package com.livk.common.redis.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author livk
 */
@Data
@Accessors(chain = true)
public class LivkMessage<T> implements Serializable {

	public static final String CHANNEL = "livk-topic";

	@Serial
	private static final long serialVersionUID = 8632296967087444509L;

	/**
	 * 公告信息id
	 */
	private Long id;

	/**
	 * 公告内容
	 */
	private String msg;

	private T data;

	public static <T> LivkMessage<T> of() {
		return new LivkMessage<>();
	}

}
