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

package com.livk.context.lock.annotation;

import com.livk.context.lock.LockType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Lock
 * </p>
 *
 * @author livk
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnLock {

	/**
	 * 锁名称
	 * @return the string
	 */
	String key();

	/**
	 * Type lock type.
	 * @return the lock type
	 */
	LockType type() default LockType.LOCK;

	/**
	 * Lease time long.
	 * @return the long
	 */
	long leaseTime() default 10;

	/**
	 * Wait time long.
	 * @return the long
	 */
	long waitTime() default 3;

	/**
	 * @return the boolean
	 */
	boolean async() default false;

}
