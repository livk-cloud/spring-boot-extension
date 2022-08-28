package com.livk.mapstruct.factory;

import com.livk.mapstruct.converter.Converter;
import com.livk.mapstruct.converter.MapstructRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * <p>
 * MapstructFactory
 * </p>
 *
 * @author livk
 * @date 2022/5/11
 */
@RequiredArgsConstructor
public class MapstructFactory implements BeanFactoryPostProcessor {

    private final MapstructRegistry registry;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.getBeanProvider(Converter.class).forEach(registry::addConverter);
    }
}
