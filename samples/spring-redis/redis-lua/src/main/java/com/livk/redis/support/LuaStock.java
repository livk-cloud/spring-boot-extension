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

package com.livk.redis.support;

import com.livk.context.redis.RedisOps;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author livk
 */
@Component
@RequiredArgsConstructor
public class LuaStock {

	private final RedisOps redisOps;

	public String buy(Integer num) {
		RedisScript<Long> redisScript = RedisScript.of(new ClassPathResource("good.lua"), Long.class);
		Long result = redisOps.execute(redisScript, List.of("stock"), num);
		Assert.notNull(result, "RedisScript Result is Null!");
		if (0 == result) {
			return "没了";
		}
		else if (2 == result) {
			return "抢到了";
		}
		return "";
	}

}
