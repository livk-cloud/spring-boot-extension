package com.livk.mapstruct.factory;

import com.livk.mapstruct.commom.Converter;
import com.livk.mapstruct.support.ConverterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * <p>
 * 解决is not eligible for getting processed by all BeanPostProcessors (for example: not
 * eligible for auto-proxying)
 * </p>
 * <p>
 * 使用@Bean直接注册{@link org.springframework.beans.factory.config.BeanPostProcessor}.
 * </p>
 * <p>
 * 出现not eligible for auto-proxying，好像是XXXAutoConfig问题.
 * </p>
 * <p>
 * BeanPostProcessor在XXXAutoConfig注册
 * </p>
 *
 * @author livk
 * @date 2022/5/13
 */
@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
public class MapstructFactoryRegister implements BeanFactoryPostProcessor {

	private final ConverterRepository<Converter> factory;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		beanFactory.addBeanPostProcessor(new MapstructFactory(factory));
	}

}
