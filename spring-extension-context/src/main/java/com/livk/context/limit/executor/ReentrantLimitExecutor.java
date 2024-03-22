/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.context.limit.executor;

import com.livk.commons.util.WebUtils;
import com.livk.context.limit.LimitExecutor;
import jakarta.servlet.http.HttpServletRequest;

import java.util.concurrent.TimeUnit;

/**
 * The type Reentrant limit executor.
 *
 * @author livk
 */
public abstract class ReentrantLimitExecutor implements LimitExecutor {

	/**
	 * The constant ATTRIBUTE_NAME.
	 */
	public static final String ATTRIBUTE_NAME = "limit";

	/**
	 * 同一个request仅只被限流一次 在给定的时间段里最多的访问限制次数(超出次数返回false)；等下个时间段开始，才允许再次被访问(返回true)，周而复始
	 * @param compositeKey 资源Key
	 * @param rate 最多的访问限制次数
	 * @param rateInterval 给定的时间段(单位秒)
	 * @param rateIntervalUnit the rate interval unit
	 * @return boolean
	 */
	protected abstract boolean reentrantTryAccess(String compositeKey, int rate, int rateInterval,
			TimeUnit rateIntervalUnit);

	@Override
	public final boolean tryAccess(String compositeKey, int rate, int rateInterval, TimeUnit rateIntervalUnit) {
		HttpServletRequest request = WebUtils.request();
		Object limitAttribute = request.getAttribute(ATTRIBUTE_NAME);
		if (limitAttribute instanceof Boolean bool && bool) {
			return true;
		}
		else {
			boolean bool = reentrantTryAccess(compositeKey, rate, rateInterval, rateIntervalUnit);
			request.setAttribute(ATTRIBUTE_NAME, bool);
			return bool;
		}
	}

}
