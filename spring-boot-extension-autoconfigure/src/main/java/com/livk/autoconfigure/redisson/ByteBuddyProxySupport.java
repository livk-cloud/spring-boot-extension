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

package com.livk.autoconfigure.redisson;

import com.google.common.collect.Sets;
import com.livk.commons.util.BeanUtils;
import com.livk.commons.util.ClassUtils;
import io.netty.channel.EventLoopGroup;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
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
 * @author livk
 */
class ByteBuddyProxySupport {

	private static final String PACKAGE_NAME = "com.livk.autoconfigure.redisson.";

	private static final Set<Class<?>> supportType = Sets.newHashSet(
		Config.class, AddressResolverGroupFactory.class, Codec.class, RedissonNodeInitializer.class,
		LoadBalancer.class, NatMapper.class, NameMapper.class, NettyHook.class, CredentialsResolver.class,
		EventLoopGroup.class, ConnectionListener.class, ExecutorService.class, KeyManagerFactory.class,
		TrustManagerFactory.class, CommandMapper.class);

	public static Set<ConfigBaseConverter<?>> proxyConverters() {
		return supportType.stream()
			.map(ByteBuddyProxySupport::proxy)
			.collect(Collectors.toSet());
	}

	@SuppressWarnings("unchecked")
	private static ConfigBaseConverter<?> proxy(Class<?> type) {
		TypeDefinition description = TypeDescription.Generic.Builder
			.parameterizedType(ConfigBaseConverter.class, type).build();
		String name = PACKAGE_NAME + type.getSimpleName() + ConfigBaseConverter.class.getSimpleName()  + "$Proxy";
		DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition<?> typeDefinition = new ByteBuddy()
			.subclass(description)
			.name(name)
			.method(ElementMatchers.none())
			.withoutCode();
		try (DynamicType.Unloaded<?> unloaded = typeDefinition.make()) {
			Class<? extends ConfigBaseConverter<?>> proxyType = (Class<? extends ConfigBaseConverter<?>>) unloaded.load(ClassUtils.getDefaultClassLoader()).getLoaded();
			return BeanUtils.instantiateClass(proxyType);
		}
	}
}
