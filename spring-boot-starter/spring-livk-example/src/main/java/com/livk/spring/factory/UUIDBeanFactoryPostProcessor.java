package com.livk.spring.factory;

import com.livk.auto.service.annotation.SpringFactories;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * <p>
 * UUIDBeanFactoryPostProcessor
 * </p>
 *
 * @author livk
 * @date 2023/1/2
 */
//@Component
@SpringFactories(type = BeanFactoryPostProcessor.class)
public class UUIDBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerResolvableDependency(UUIDRequest.class, new UUIDObjectFactory());
    }
}
