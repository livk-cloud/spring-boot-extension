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

package com.livk.spring.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;

/**
 * <p>
 * {@see AutowireUtils#resolveAutowiringValue(Object, Class)}
 * 需要注册interface并且当前类实现{@link Serializable} 则会被spring代理
 * </p>
 *
 * @author livk
 */
public class UUIDObjectFactory implements ObjectFactory<UUIDRequest>, Serializable {

	@NonNull
	@Override
	public UUIDRequest getObject() throws BeansException {
		return UUIDConTextHolder::get;
	}

}
