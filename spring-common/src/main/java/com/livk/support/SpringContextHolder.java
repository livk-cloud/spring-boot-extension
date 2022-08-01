package com.livk.support;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * <p>
 * SpringContextHolder
 * </p>
 *
 * @author livk
 * @date 2021/10/22
 */
@Slf4j
@Component
public class SpringContextHolder implements BeanFactoryPostProcessor, ApplicationContextAware, DisposableBean {

    @Getter
    private static ApplicationContext applicationContext = null;

    private static ConfigurableListableBeanFactory beanFactory = null;

    /**
     * Spring事件发布
     *
     * @param event 事件
     */
    public static void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) getBeanFactory().getBean(name);
    }

    public static <T> T getBean(Class<T> typeClass) {
        return getBeanFactory().getBean(typeClass);
    }

    public static <T> T getBean(String name, Class<T> typeClass) {
        return getBeanFactory().getBean(name, typeClass);
    }

    public static <T> Collection<T> getBeansOfType(Class<T> typeClass) {
        return getBeanFactory().getBeansOfType(typeClass).values();
    }

    public static String getProperty(String key) {
        return getProperty(key, String.class);
    }

    public static <T> T getProperty(String key, Class<T> requiredType) {
        return applicationContext.getEnvironment().getProperty(key, requiredType);
    }

    public static <T> T getProperty(String key, Class<T> requiredType, T defaultValue) {
        return applicationContext.getEnvironment().getProperty(key, requiredType, defaultValue);
    }

    /**
     * 解析文本占位符${**}
     *
     * @param text the String to resolve
     */
    public static String resolvePlaceholders(String text) {
        return applicationContext.getEnvironment().resolvePlaceholders(text);
    }

    /**
     * 注册bean
     *
     * @param bean     bean实例
     * @param beanName beanName可为空，为空会自动生成
     * @param <T>      bean类型
     */
    public static <T> void registerBean(T bean, String beanName) {
        if (null != beanFactory) {
            DefaultListableBeanFactory defaultBeanFactory = (DefaultListableBeanFactory) beanFactory;
            registerBean(defaultBeanFactory, bean, beanName);
        } else if (applicationContext instanceof GenericApplicationContext context) {
            registerBean(context, bean, beanName);
        } else {
            log.error("bean register fail name:{} instant:{}", beanName, bean);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void registerBean(BeanDefinitionRegistry registry, T bean, String beanName) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition((Class<T>) bean.getClass(), () -> bean);
        beanName = StringUtils.hasText(beanName) ? beanName :
                BeanDefinitionReaderUtils.generateBeanName(beanDefinition, registry);
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    private static ListableBeanFactory getBeanFactory() {
        return beanFactory != null ? beanFactory : applicationContext;
    }

    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (SpringContextHolder.beanFactory != null) {
            log.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:{}",
                    SpringContextHolder.applicationContext);
        }
        synchronized (SpringContextHolder.class) {
            SpringContextHolder.beanFactory = beanFactory;
        }
    }

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        if (SpringContextHolder.applicationContext != null) {
            log.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:{}",
                    SpringContextHolder.applicationContext);
        }
        synchronized (SpringContextHolder.class) {
            SpringContextHolder.applicationContext = applicationContext;
        }
    }

    @Override
    public void destroy() {
        if (log.isDebugEnabled()) {
            log.debug("清除SpringContextHolder中的ApplicationContext:{}", applicationContext);
            log.debug("清除SpringContextHolder中的beanFactory:{}", beanFactory);
        }
        synchronized (SpringContextHolder.class) {
            SpringContextHolder.applicationContext = null;
            SpringContextHolder.beanFactory = null;
        }
    }
}
