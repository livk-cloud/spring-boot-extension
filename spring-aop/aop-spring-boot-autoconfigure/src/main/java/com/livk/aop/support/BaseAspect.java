package com.livk.aop.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * <p>
 * BaseAspect
 * </p>
 *
 * @author livk
 * @date 2022/7/6
 */
public abstract class BaseAspect<T> implements ImportBeanDefinitionRegistrar, BeanFactoryAware {
    protected DefaultListableBeanFactory listableBeanFactory;

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        listableBeanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        Class<T> typeClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), BaseAspect.class);
        listableBeanFactory.getBeansOfType(typeClass)
                .values()
                .forEach(annotationIntercept -> {
                    BeanDefinition beanDefinition = getBeanDefinition(annotationIntercept);
                    String beanName = BeanDefinitionReaderUtils.generateBeanName(beanDefinition, registry);
                    registry.registerBeanDefinition(beanName, beanDefinition);
                });
    }

    protected abstract BeanDefinition getBeanDefinition(T t);
}
