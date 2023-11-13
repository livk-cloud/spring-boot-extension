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
	public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
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
