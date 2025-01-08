/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.autoconfigure.redisson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.livk.commons.jackson.core.JacksonSupport;
import io.netty.channel.EventLoopGroup;
import lombok.RequiredArgsConstructor;
import org.redisson.api.NameMapper;
import org.redisson.api.NatMapper;
import org.redisson.api.RedissonNodeInitializer;
import org.redisson.client.FailedNodeDetector;
import org.redisson.client.NettyHook;
import org.redisson.client.codec.Codec;
import org.redisson.codec.ReferenceCodecProvider;
import org.redisson.config.BaseMasterSlaveServersConfig;
import org.redisson.config.CommandMapper;
import org.redisson.config.Config;
import org.redisson.config.ConfigSupport;
import org.redisson.config.CredentialsResolver;
import org.redisson.connection.AddressResolverGroupFactory;
import org.redisson.connection.ConnectionListener;
import org.redisson.connection.balancer.LoadBalancer;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.beans.PropertyEditorSupport;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author livk
 */
final class RedissonPropertyEditorRegistrar implements PropertyEditorRegistrar {

	private static final Set<Class<?>> supportType = Set.of(Config.class, AddressResolverGroupFactory.class,
			Codec.class, RedissonNodeInitializer.class, LoadBalancer.class, NatMapper.class, NameMapper.class,
			NettyHook.class, CredentialsResolver.class, EventLoopGroup.class, ConnectionListener.class,
			ExecutorService.class, KeyManagerFactory.class, TrustManagerFactory.class, CommandMapper.class);

	@Override
	public void registerCustomEditors(PropertyEditorRegistry registry) {
		supportType.stream()
			.collect(Collectors.toMap(Function.identity(), TypePropertyEditor::new))
			.forEach((registry::registerCustomEditor));
	}

	@RequiredArgsConstructor
	private static class TypePropertyEditor extends PropertyEditorSupport {

		private static final JacksonSupport support = create();

		private final Class<?> type;

		@Override
		public void setAsText(String text) {
			setValue(support.readValue(text, type));
		}

		private static JacksonSupport create() {
			YAMLMapper mapper = new YAMLMapper();
			mapper.addMixIn(Config.class, ConfigSupport.ConfigMixIn.class);
			mapper.addMixIn(BaseMasterSlaveServersConfig.class, ConfigSupport.ConfigPropsMixIn.class);
			mapper.addMixIn(ReferenceCodecProvider.class, ConfigSupport.ClassMixIn.class);
			mapper.addMixIn(AddressResolverGroupFactory.class, ConfigSupport.ClassMixIn.class);
			mapper.addMixIn(Codec.class, ConfigSupport.ClassMixIn.class);
			mapper.addMixIn(RedissonNodeInitializer.class, ConfigSupport.ClassMixIn.class);
			mapper.addMixIn(LoadBalancer.class, ConfigSupport.ClassMixIn.class);
			mapper.addMixIn(NatMapper.class, ConfigSupport.ClassMixIn.class);
			mapper.addMixIn(NameMapper.class, ConfigSupport.ClassMixIn.class);
			mapper.addMixIn(NettyHook.class, ConfigSupport.ClassMixIn.class);
			mapper.addMixIn(CredentialsResolver.class, ConfigSupport.ClassMixIn.class);
			mapper.addMixIn(EventLoopGroup.class, ConfigSupport.ClassMixIn.class);
			mapper.addMixIn(ConnectionListener.class, ConfigSupport.ClassMixIn.class);
			mapper.addMixIn(ExecutorService.class, ConfigSupport.ClassMixIn.class);
			mapper.addMixIn(KeyManagerFactory.class, ConfigSupport.IgnoreMixIn.class);
			mapper.addMixIn(TrustManagerFactory.class, ConfigSupport.IgnoreMixIn.class);
			mapper.addMixIn(CommandMapper.class, ConfigSupport.ClassMixIn.class);
			mapper.addMixIn(FailedNodeDetector.class, ConfigSupport.ClassMixIn.class);
			FilterProvider filterProvider = new SimpleFilterProvider().addFilter("classFilter",
					SimpleBeanPropertyFilter.filterOutAllExcept());
			mapper.setFilterProvider(filterProvider);
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			return new JacksonSupport(mapper);
		}

	}

}
