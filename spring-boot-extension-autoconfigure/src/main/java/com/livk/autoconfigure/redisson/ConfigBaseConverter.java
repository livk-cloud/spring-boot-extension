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
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.util.concurrent.ExecutorService;

/**
 * The type Config base converter.
 *
 * @param <T> the type parameter
 * @author livk
 */
@RequiredArgsConstructor
public abstract class ConfigBaseConverter<T> implements Converter<String, T> {

    /**
     * The type Config converter.
     */
    public static class ConfigConverter extends ConfigBaseConverter<Config> {
    }

    /**
     * The type Address resolver group factory converter.
     */
    public static class AddressResolverGroupFactoryConverter extends ConfigBaseConverter<AddressResolverGroupFactory> {
    }

    /**
     * The type Codec converter.
     */
    public static class CodecConverter extends ConfigBaseConverter<Codec> {
    }

    /**
     * The type Redisson node initializer converter.
     */
    public static class RedissonNodeInitializerConverter extends ConfigBaseConverter<RedissonNodeInitializer> {
    }

    /**
     * The type Load balancer converter.
     */
    public static class LoadBalancerConverter extends ConfigBaseConverter<LoadBalancer> {
    }

    /**
     * The type Nat mapper converter.
     */
    public static class NatMapperConverter extends ConfigBaseConverter<NatMapper> {
    }

    /**
     * The type Name mapper converter.
     */
    public static class NameMapperConverter extends ConfigBaseConverter<NameMapper> {
    }

    /**
     * The type Netty hook converter.
     */
    public static class NettyHookConverter extends ConfigBaseConverter<NettyHook> {
    }

    /**
     * The type Credentials resolver converter.
     */
    public static class CredentialsResolverConverter extends ConfigBaseConverter<CredentialsResolver> {
    }

    /**
     * The type Event loop group converter.
     */
    public static class EventLoopGroupConverter extends ConfigBaseConverter<EventLoopGroup> {
    }

    /**
     * The type Connection listener converter.
     */
    public static class ConnectionListenerConverter extends ConfigBaseConverter<ConnectionListener> {
    }

    /**
     * The type Executor service converter.
     */
    public static class ExecutorServiceConverter extends ConfigBaseConverter<ExecutorService> {
    }

    /**
     * The type Key manager factory converter.
     */
    public static class KeyManagerFactoryConverter extends ConfigBaseConverter<KeyManagerFactory> {
    }

    /**
     * The type Trust manager factory converter.
     */
    public static class TrustManagerFactoryConverter extends ConfigBaseConverter<TrustManagerFactory> {
    }

    /**
     * The type Command mapper converter.
     */
    public static class CommandMapperConverter extends ConfigBaseConverter<CommandMapper> {
    }


    @SuppressWarnings("unchecked")
    private final Class<T> type = (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), ConfigBaseConverter.class);

	private static final JacksonSupport<YAMLMapper> support = JacksonSupport.create(createMapper());

    @Override
    public T convert(@NonNull String source) {
        return support.readValue(source, type);
    }

    private static YAMLMapper createMapper() {
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
        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("classFilter", SimpleBeanPropertyFilter.filterOutAllExcept());
        mapper.setFilterProvider(filterProvider);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper;
    }
}
