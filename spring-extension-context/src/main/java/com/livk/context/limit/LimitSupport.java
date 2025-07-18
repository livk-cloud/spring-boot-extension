/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.context.limit;

import com.livk.commons.expression.ExpressionResolver;
import com.livk.commons.expression.spring.SpringExpressionResolver;
import com.livk.commons.util.HttpServletUtils;
import com.livk.context.limit.annotation.Limit;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * The type Limit support.
 *
 * @author livk
 */
@RequiredArgsConstructor(staticName = "of")
public class LimitSupport {

	/**
	 * SpEL表达式解析器
	 */
	private final ExpressionResolver resolver = new SpringExpressionResolver();

	private final LimitExecutor limitExecutor;

	/**
	 * Exec boolean.
	 * @param limit the limit
	 * @param method the method
	 * @param args the args
	 * @return the boolean
	 */
	public boolean exec(Limit limit, Method method, Object[] args) {
		String key = limit.key();
		int rate = limit.rate();
		int rateInterval = limit.rateInterval();
		TimeUnit unit = limit.rateIntervalUnit();
		String spELKey = resolver.evaluate(key, method, args);
		if (limit.restrictIp()) {
			String ip = HttpServletUtils.realIp(HttpServletUtils.request());
			spELKey = spELKey + "#" + ip;
		}
		return limitExecutor.tryAccess(spELKey, rate, Duration.ofMillis(unit.toMillis(rateInterval)));
	}

}
