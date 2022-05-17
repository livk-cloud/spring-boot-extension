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
@RequiredArgsConstructor
public class MapstructFactory implements BeanPostProcessor {

	private final ConverterRepository factory;

	@SuppressWarnings("rawtypes")
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof Converter) {
			factory.put((Converter) bean);
		}
		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
	}

}
