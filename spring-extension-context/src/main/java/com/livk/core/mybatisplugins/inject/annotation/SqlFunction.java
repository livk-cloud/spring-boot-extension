/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.core.mybatisplugins.inject.annotation;

import com.livk.core.mybatisplugins.inject.enums.FunctionType;
import com.livk.core.mybatisplugins.inject.enums.SqlFill;
import com.livk.core.mybatisplugins.inject.handler.FunctionHandle;
import com.livk.core.mybatisplugins.inject.handler.NullFunction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * SqlFunction
 * </p>
 *
 * @author livk
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SqlFunction {

	/**
	 * <p>
	 * {@link SqlFunction#supplier()}
	 * </p>
	 * <p>
	 * {@link SqlFunction#time()}
	 * </p>
	 * <p>
	 * 两个必须指定一个，否则无法注入
	 * </p>
	 * @return the sql fill
	 */
	SqlFill fill();

	/**
	 * 优先级低于{@link SqlFunction#time()}
	 * @return the class
	 */
	Class<? extends FunctionHandle<?>> supplier() default NullFunction.class;

	/**
	 * 优先级高于 {@link SqlFunction#supplier()}
	 * @return the function enum
	 */
	FunctionType time() default FunctionType.DEFAULT;

}
