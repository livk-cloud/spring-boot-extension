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

package com.livk.autoconfigure.redisson;

import com.livk.auto.service.annotation.SpringAutoService;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;

/**
 * The type Redisson auto configuration.
 *
 * @author livk
 */
@SpringAutoService
@ConditionalOnClass(Redisson.class)
@AutoConfiguration(before = RedisAutoConfiguration.class)
@EnableConfigurationProperties({ConfigProperties.class, RedisProperties.class})
public class RedissonAutoConfiguration {

    /**
     * Codec converter bean factory post processor bean factory post processor.
     *
     * @param baseConverters the base converters
     * @return the bean factory post processor
     */
    @Bean
    public BeanFactoryPostProcessor codecConverterBeanFactoryPostProcessor(ObjectProvider<ConfigBaseConverter<?>> baseConverters) {
        return beanFactory -> {
            ConversionService conversionService = beanFactory.getConversionService();
            if (conversionService instanceof ConverterRegistry converterRegistry) {
                converterRegistry.addConverter(new ConfigBaseConverter.ConfigConverter());
                converterRegistry.addConverter(new ConfigBaseConverter.AddressResolverGroupFactoryConverter());
                converterRegistry.addConverter(new ConfigBaseConverter.CodecConverter());
                converterRegistry.addConverter(new ConfigBaseConverter.RedissonNodeInitializerConverter());
                converterRegistry.addConverter(new ConfigBaseConverter.LoadBalancerConverter());
                converterRegistry.addConverter(new ConfigBaseConverter.NatMapperConverter());
                converterRegistry.addConverter(new ConfigBaseConverter.NameMapperConverter());
                converterRegistry.addConverter(new ConfigBaseConverter.NettyHookConverter());
                converterRegistry.addConverter(new ConfigBaseConverter.CredentialsResolverConverter());
                converterRegistry.addConverter(new ConfigBaseConverter.EventLoopGroupConverter());
                converterRegistry.addConverter(new ConfigBaseConverter.ConnectionListenerConverter());
                converterRegistry.addConverter(new ConfigBaseConverter.ExecutorServiceConverter());
                converterRegistry.addConverter(new ConfigBaseConverter.KeyManagerFactoryConverter());
                converterRegistry.addConverter(new ConfigBaseConverter.TrustManagerFactoryConverter());
                converterRegistry.addConverter(new ConfigBaseConverter.CommandMapperConverter());
                baseConverters.orderedStream().forEach(converterRegistry::addConverter);
            }
        };
    }

    /**
     * Redisson client redisson client.
     *
     * @param configProperties  the config properties
     * @param redisProperties   the redis properties
     * @param configCustomizers the config customizers
     * @return the redisson client
     */
    @Bean
	@ConditionalOnMissingBean
    public RedissonClient redissonClient(ConfigProperties configProperties,
                                         RedisProperties redisProperties,
                                         ObjectProvider<ConfigCustomizer> configCustomizers) {
        return RedissonClientFactory.create(configProperties, redisProperties, configCustomizers);
    }

	@Bean
	@ConditionalOnMissingBean
	public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
		return new RedissonConnectionFactory(redisson);
	}
}
