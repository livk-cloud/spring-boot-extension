package com.livk.http.factory;

import com.livk.http.annotation.BeanName;
import com.livk.util.SpringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.NonNull;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * HttpFactory
 * </p>
 *
 * @author livk
 * @date 2022/5/20
 */
@RequiredArgsConstructor
public class HttpServiceRegistrar implements BeanFactoryPostProcessor, ResourceLoaderAware {

    private final HttpServiceProxyFactory proxyFactory;

    private ResourceLoader resourceLoader;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        List<String> packages = AutoConfigurationPackages.get(beanFactory);
        Set<Class<?>> typesAnnotatedClass = SpringUtils.findByAnnotationType(HttpExchange.class, resourceLoader,
                packages.toArray(String[]::new));
        for (Class<?> exchangeClass : typesAnnotatedClass) {
            BeanDefinition beanDefinition = getBeanDefinition(exchangeClass);
            BeanName name = AnnotationUtils.getAnnotation(exchangeClass, BeanName.class);
            String beanName = name != null ? name.value()
                    : BeanDefinitionReaderUtils.generateBeanName(beanDefinition, defaultListableBeanFactory);
            defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinition);
        }
    }

    private <T> BeanDefinition getBeanDefinition(Class<T> exchangeClass) {
        return new RootBeanDefinition(exchangeClass, () -> proxyFactory.createClient(exchangeClass));
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
