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

package com.livk.redis.submit;

import com.livk.commons.util.WebUtils;
import com.livk.core.lock.LockType;
import com.livk.redis.submit.annotation.AutoRepeatedSubmit;
import com.livk.redis.submit.support.LockSupport;
import com.livk.redis.submit.support.RedisSupport;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class AutoRepeatedSubmitInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(@NonNull HttpServletRequest request,
							 @NonNull HttpServletResponse response,
							 @NonNull Object handler) {
		if (handler instanceof HandlerMethod handlerMethod) {
			AutoRepeatedSubmit methodAnnotation = handlerMethod.getMethodAnnotation(AutoRepeatedSubmit.class);
			if (methodAnnotation != null) {
				String token = WebUtils.request().getHeader(HttpHeaders.AUTHORIZATION);
				if (StringUtils.hasText(token)) {
					try {
						if (LockSupport.tryLock(LockType.LOCK, token, 3, 3, false)) {
							if (RedisSupport.exists(token)) {
								WebUtils.outJson(response, "重复提交3s后重试");
								return false;
							} else {
								RedisSupport.setEx(token, true, 3L);
								return true;
							}
						} else {
							WebUtils.outJson(response, "重复提交3s后重试");
							return false;
						}
					} finally {
						LockSupport.unlock();
					}
				}
				WebUtils.outJson(response, "丢失Token");
				return false;
			}
		}
		//必须返回true,否则会被拦截一切请求
		return true;
	}
}
