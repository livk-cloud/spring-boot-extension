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

package com.livk.autoconfigure.redisson;

import org.junit.jupiter.api.Test;
import org.redisson.client.DefaultNettyHook;
import org.redisson.client.NettyHook;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.connection.AddressResolverGroupFactory;
import org.redisson.connection.SequentialDnsAddressResolverFactory;
import org.springframework.beans.SimpleTypeConverter;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author livk
 */
class RedissonPropertyEditorRegistrarTest {

	@Test
	void test() {
		SimpleTypeConverter converter = new SimpleTypeConverter();

		RedissonPropertyEditorRegistrar registrar = new RedissonPropertyEditorRegistrar();
		registrar.registerCustomEditors(converter);

		Codec codec = converter.convertIfNecessary("!<org.redisson.codec.JsonJacksonCodec> {}", Codec.class);
		assertNotNull(codec);
		assertInstanceOf(JsonJacksonCodec.class, codec);

		NettyHook hook = converter.convertIfNecessary("!<org.redisson.client.DefaultNettyHook> {}", NettyHook.class);
		assertNotNull(hook);
		assertInstanceOf(DefaultNettyHook.class, hook);

		AddressResolverGroupFactory factory = converter.convertIfNecessary(
				"!<org.redisson.connection.SequentialDnsAddressResolverFactory> {}", AddressResolverGroupFactory.class);
		assertNotNull(factory);
		assertInstanceOf(SequentialDnsAddressResolverFactory.class, factory);
	}

}
