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

package com.livk.redis.submit.support;

import com.livk.commons.SpringContextHolder;
import com.livk.context.redis.RedisOps;
import lombok.experimental.UtilityClass;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * The type Redis support.
 *
 * @author livk
 */
@UtilityClass
public class RedisSupport {

	private static final ValueOperations<String, Object> OPERATIONS;

	private static final RedisOps REDIS_OPS;

	static {
		REDIS_OPS = SpringContextHolder.getBean(RedisOps.class);
		OPERATIONS = REDIS_OPS.opsForValue();
	}

	/**
	 * 写入缓存设置时效时间
	 * @param key the key
	 * @param value the value
	 * @param expireTime the expire time
	 */
	public void setEx(String key, Object value, Long expireTime) {
		OPERATIONS.set(key, value, expireTime, TimeUnit.SECONDS);
	}

	/**
	 * 判断缓存中是否有对应的value
	 * @param key the key
	 * @return boolean boolean
	 */
	public boolean exists(String key) {
		return REDIS_OPS.hasKey(key);
	}

}
