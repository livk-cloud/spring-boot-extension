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

package com.livk.context.limit.annotation;

import com.livk.context.limit.exception.LimitExceededHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * The interface Limit.
 *
 * @author livk
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

	/**
	 * 资源的key
	 * @return String string
	 */
	String key() default "";

	/**
	 * 单位时间
	 * @return int int
	 */
	int rateInterval();

	/**
	 * 单位(默认秒)
	 * @return TimeUnit time unit
	 */
	TimeUnit rateIntervalUnit() default TimeUnit.SECONDS;

	/**
	 * 单位时间产生的令牌个数
	 * @return int int
	 */
	int rate();

	/**
	 * 是否限制IP
	 * @return boolean boolean
	 */
	boolean restrictIp() default false;

	/**
	 * 异常处理器
	 */
	Class<? extends LimitExceededHandler> handler() default LimitExceededHandler.class;

}
