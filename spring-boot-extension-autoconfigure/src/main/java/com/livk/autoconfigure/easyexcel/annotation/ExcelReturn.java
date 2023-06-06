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

package com.livk.autoconfigure.easyexcel.annotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>在Mvc的环境下支持{@link java.util.Collection}、</p>
 * <p>{@link java.util.Map}</p>
 * <p>{example List}</p>
 * <p>{example Map String, List }</p>
 * <p>在Reactive的环境下支持{@link java.util.Collection}、</p>
 * <p>{@link java.util.Map}、</p>
 * <p>{@link reactor.core.publisher.Mono}、</p>
 * <p>{@link reactor.core.publisher.Flux} </p>
 * <p>{example List}</p>
 * <p>{example Map String, List }</p>
 * <p>{example Mono List }</p>
 * <p>{example Mono Map String, List}</p>
 * <p>{example Flux }</p>
 *
 * @author livk
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelReturn {

	/**
	 * File name string.
	 *
	 * @return the string
	 */
	String fileName();

	/**
	 * Template path string.
	 *
	 * @return the string
	 */
	String template() default "";

	/**
	 * Suffix suffix.
	 *
	 * @return the suffix
	 */
	Suffix suffix() default Suffix.XLSM;

	/**
	 * The enum Suffix.
	 */
	@Getter
	@RequiredArgsConstructor
	enum Suffix {

		/**
		 * Xls suffix.
		 */
		XLS(".xls"),

		/**
		 * Xlsx suffix.
		 */
		XLSX(".xlsx"),
		/**
		 * Xlsm suffix.
		 */
		XLSM(".xlsm");

		private final String name;

	}

}
