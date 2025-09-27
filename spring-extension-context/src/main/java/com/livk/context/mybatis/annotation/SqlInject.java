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

package com.livk.context.mybatis.annotation;

import com.livk.context.mybatis.enums.InjectType;
import com.livk.context.mybatis.enums.SqlFill;
import com.livk.context.mybatis.handler.InjectHandle;
import com.livk.context.mybatis.handler.NullInject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author livk
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SqlInject {

	/**
	 * <p>
	 * {@link SqlInject#supplier()}
	 * </p>
	 * <p>
	 * {@link SqlInject#time()}
	 * </p>
	 * <p>
	 * 两个必须指定一个，否则无法注入
	 * </p>
	 * @return the sql fill
	 */
	SqlFill fill();

	/**
	 * @return the class
	 */
	Class<? extends InjectHandle<?>> supplier() default NullInject.class;

	/**
	 * 优先级高于 {@link SqlInject#supplier()}
	 * @return the function enum
	 * @deprecated use {@link SqlInject#supplier()}
	 */
	@Deprecated(since = "1.5.2")
	InjectType time() default InjectType.DEFAULT;

}
