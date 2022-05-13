package com.livk.mapstruct.factory;

import com.livk.mapstruct.commom.Converter;
import com.livk.mapstruct.support.ConverterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * <p>
 * MapstructFactory
 * </p>
 *
 * @author livk
 * @date 2022/5/11
 */
@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
public class MapstructFactory implements BeanPostProcessor {

	private final ConverterRepository<Converter> factory;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof Converter converter) {
			factory.put(converter);
		}
		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
	}

}
