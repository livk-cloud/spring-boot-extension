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

package com.livk.autoconfigure.redisson;

import com.google.common.collect.Sets;
import com.livk.commons.util.BeanUtils;
import com.livk.commons.util.ClassUtils;
import com.livk.commons.util.GenericsByteBuddy;
import io.netty.channel.EventLoopGroup;
import net.bytebuddy.dynamic.DynamicType.Unloaded;
import net.bytebuddy.matcher.ElementMatchers;
import org.redisson.api.NameMapper;
import org.redisson.api.NatMapper;
import org.redisson.api.RedissonNodeInitializer;
import org.redisson.client.NettyHook;
import org.redisson.client.codec.Codec;
import org.redisson.config.CommandMapper;
import org.redisson.config.Config;
import org.redisson.config.CredentialsResolver;
import org.redisson.connection.AddressResolverGroupFactory;
import org.redisson.connection.ConnectionListener;
import org.redisson.connection.balancer.LoadBalancer;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * The type Byte buddy proxy support.
 *
 * @author livk
 */
final class ConverterProxySupport {

	private static final String PACKAGE_NAME = ConverterProxySupport.class.getPackageName();

	private static final Set<Class<?>> supportType = Sets.newHashSet(Config.class, AddressResolverGroupFactory.class,
			Codec.class, RedissonNodeInitializer.class, LoadBalancer.class, NatMapper.class, NameMapper.class,
			NettyHook.class, CredentialsResolver.class, EventLoopGroup.class, ConnectionListener.class,
			ExecutorService.class, KeyManagerFactory.class, TrustManagerFactory.class, CommandMapper.class);

	/**
	 * Proxy converters set.
	 * @return the set
	 */
	public static Set<ConfigBaseConverter<?>> makeConverters() {
		return supportType.stream().map(ConverterProxySupport::make).collect(Collectors.toSet());
	}

	private static ConfigBaseConverter<?> make(Class<?> type) {
		String name = PACKAGE_NAME + type.getSimpleName() + ConfigBaseConverter.class.getSimpleName() + "$Proxy";
		try (Unloaded<ConfigBaseConverter<?>> unloaded = new GenericsByteBuddy()
			.<ConfigBaseConverter<?>>sub(ConfigBaseConverter.class, type)
			.name(name)
			.method(ElementMatchers.none())
			.withoutCode()
			.make()) {
			Class<? extends ConfigBaseConverter<?>> proxyType = unloaded.load(ClassUtils.getDefaultClassLoader())
				.getLoaded();
			return BeanUtils.instantiateClass(proxyType);
		}
	}

}
