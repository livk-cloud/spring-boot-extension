package com.livk.commons.spring.context;

import com.livk.auto.service.annotation.SpringAutoService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.ResolvableType;
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
@SpringAutoService
public class SpringContextHolder implements BeanFactoryAware, ApplicationContextAware, DisposableBean {

    @Getter
    private static ApplicationContext applicationContext = null;

    private static BeanFactory beanFactory = null;

    /**
     * Spring事件发布
     *
     * @param event 事件
     */
    public static void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    /**
     * Gets bean.
     *
     * @param <T>  the type parameter
     * @param name the name
     * @return the bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) getBeanFactory().getBean(name);
    }

    /**
     * Gets bean.
     *
     * @param <T>       the type parameter
     * @param typeClass the type class
     * @return the bean
     */
    public static <T> T getBean(Class<T> typeClass) {
        return getBeanFactory().getBean(typeClass);
    }

    /**
     * Gets bean.
     *
     * @param <T>       the type parameter
     * @param name      the name
     * @param typeClass the type class
     * @return the bean
     */
    public static <T> T getBean(String name, Class<T> typeClass) {
        return getBeanFactory().getBean(name, typeClass);
    }

    /**
     * Gets bean provider.
     *
     * @param <T>       the type parameter
     * @param typeClass the type class
     * @return the bean provider
     */
    public static <T> ObjectProvider<T> getBeanProvider(Class<T> typeClass) {
        return getBeanFactory().getBeanProvider(typeClass);
    }

    /**
     * Gets bean provider.
     *
     * @param <T>            the type parameter
     * @param resolvableType the resolvable type
     * @return the bean provider
     */
    public static <T> ObjectProvider<T> getBeanProvider(ResolvableType resolvableType) {
        return getBeanFactory().getBeanProvider(resolvableType);
    }

    /**
     * Gets beans of type.
     *
     * @param <T>       the type parameter
     * @param typeClass the type class
     * @return the beans of type
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> typeClass) {
        return getBeanFactory().getBeansOfType(typeClass);
    }

    /**
     * Gets property.
     *
     * @param key the key
     * @return the property
     */
    public static String getProperty(String key) {
        return getProperty(key, String.class);
    }

    /**
     * Gets property.
     *
     * @param <T>          the type parameter
     * @param key          the key
     * @param requiredType the required type
     * @return the property
     */
    public static <T> T getProperty(String key, Class<T> requiredType) {
        return applicationContext.getEnvironment().getProperty(key, requiredType);
    }

    /**
     * Gets property.
     *
     * @param <T>          the type parameter
     * @param key          the key
     * @param requiredType the required type
     * @param defaultValue the default value
     * @return the property
     */
    public static <T> T getProperty(String key, Class<T> requiredType, T defaultValue) {
        return applicationContext.getEnvironment().getProperty(key, requiredType, defaultValue);
    }

    /**
     * 解析文本占位符${**}
     *
     * @param text the String to resolve
     * @return the string
     */
    public static String resolvePlaceholders(String text) {
        return applicationContext.getEnvironment().resolvePlaceholders(text);
    }

    /**
     * 注册bean
     *
     * @param <T>      bean类型
     * @param bean     bean实例
     * @param beanName beanName可为空，为空会自动生成
     */
    public static <T> void registerBean(T bean, String beanName) {
        ResolvableType resolvableType = ResolvableType.forInstance(bean);
        BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(resolvableType, () -> bean);
        registerBean(definitionBuilder.getBeanDefinition(), beanName);
    }

    /**
     * Register bean.
     *
     * @param beanDefinition the bean definition
     * @param beanName       the bean name
     */
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
        return beanFactory instanceof ListableBeanFactory listableBeanFactory ? listableBeanFactory : applicationContext;
    }

    @Override
    public synchronized void setBeanFactory(@Nullable BeanFactory beanFactory) throws BeansException {
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
