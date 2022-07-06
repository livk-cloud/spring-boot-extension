package com.livk.support;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

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
public class SpringContextHolder implements BeanFactoryAware, ApplicationContextAware, DisposableBean {

    @Getter
    private static ApplicationContext applicationContext = null;

    private static ListableBeanFactory beanFactory = null;

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
        return (T) beanFactory.getBean(name);
    }

    public static <T> T getBean(Class<T> typeClass) {
        return beanFactory.getBean(typeClass);
    }

    public static <T> T getBean(String name, Class<T> typeClass) {
        return beanFactory.getBean(name, typeClass);
    }

    public static <T> Collection<T> getBeansOfType(Class<T> typeClass) {
        return beanFactory.getBeansOfType(typeClass).values();
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

    public static String resolvePlaceholders(String text) {
        return applicationContext.getEnvironment().resolvePlaceholders(text);
    }

    @Override
    public void setBeanFactory(@Nullable BeanFactory beanFactory) throws BeansException {
        if (SpringContextHolder.beanFactory != null) {
            log.warn("SpringContextHolder中的beanFactory被覆盖, 原有beanFactory为:{}",
                    SpringContextHolder.beanFactory);
        }
        synchronized (SpringContextHolder.class) {
            SpringContextHolder.beanFactory = (ListableBeanFactory) beanFactory;
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
