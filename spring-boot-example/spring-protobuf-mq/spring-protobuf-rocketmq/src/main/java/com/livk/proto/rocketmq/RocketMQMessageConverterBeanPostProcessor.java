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

package com.livk.proto.rocketmq;

import com.livk.proto.rocketmq.converter.UserProtobufMessageConverter;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author livk
 */
@Component
public class RocketMQMessageConverterBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName)
			throws BeansException {
		if (bean instanceof RocketMQMessageConverter rocketMQMessageConverter) {
			MessageConverter messageConverter = rocketMQMessageConverter.getMessageConverter();
			if (messageConverter instanceof CompositeMessageConverter compositeMessageConverter) {
				List<MessageConverter> converters = compositeMessageConverter.getConverters();
				converters.add(new UserProtobufMessageConverter());
			}
			return rocketMQMessageConverter;
		}
		return bean;
	}

}
