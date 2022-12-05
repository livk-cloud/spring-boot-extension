package com.livk.commons.support;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
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
import org.springframework.core.ResolvableType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * <p>
 * SpringContextHolder
 * </p>
 *
 * @author livk
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

    public static <T> ObjectProvider<T> getBeanProvider(Class<T> typeClass) {
        return getBeanFactory().getBeanProvider(typeClass);
    }

    public static <T> ObjectProvider<T> getBeanProvider(ResolvableType resolvableType) {
        return getBeanFactory().getBeanProvider(resolvableType);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> typeClass) {
        return getBeanFactory().getBeansOfType(typeClass);
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
    @SuppressWarnings("unchecked")
    public static <T> void registerBean(T bean, String beanName) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition((Class<T>) bean.getClass(), () -> bean);
        registerBean(beanDefinition, beanName);
    }

    public static void registerBean(BeanDefinition beanDefinition, String beanName) {
        if (beanFactory instanceof DefaultListableBeanFactory defaultBeanFactory) {
            registerBean(defaultBeanFactory, beanDefinition, beanName);
        } else if (applicationContext instanceof GenericApplicationContext context) {
            registerBean(context, beanDefinition, beanName);
        } else {
            log.error("bean register fail name:{} instantClass:{}", beanName, beanDefinition.getBeanClassName());
        }
    }

    private static void registerBean(BeanDefinitionRegistry registry, BeanDefinition beanDefinition, String beanName) {
        beanName = StringUtils.hasText(beanName) ? beanName :
                BeanDefinitionReaderUtils.generateBeanName(beanDefinition, registry);
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    private static ListableBeanFactory getBeanFactory() {
        return beanFactory != null ? beanFactory : applicationContext;
    }

    @Override
    public synchronized void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringContextHolder.beanFactory = beanFactory;
    }

    @Override
    public synchronized void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    @Override
    public synchronized void destroy() {
        SpringContextHolder.applicationContext = null;
        SpringContextHolder.beanFactory = null;
    }
}
