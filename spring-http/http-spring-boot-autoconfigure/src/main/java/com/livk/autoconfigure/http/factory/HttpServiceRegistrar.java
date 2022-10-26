package com.livk.autoconfigure.http.factory;

import com.livk.autoconfigure.http.annotation.BeanName;
import com.livk.util.SpringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
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
public class HttpServiceRegistrar implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware, BeanFactoryAware {

    private final HttpServiceProxyFactory proxyFactory;

    private ResourceLoader resourceLoader;

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        List<String> packages = AutoConfigurationPackages.get(beanFactory);
        Set<Class<?>> typesAnnotatedClass = SpringUtils.findByAnnotationType(HttpExchange.class, resourceLoader,
                packages.toArray(String[]::new));
        for (Class<?> exchangeClass : typesAnnotatedClass) {
            BeanDefinition beanDefinition = getBeanDefinition(exchangeClass);
            BeanName name = AnnotationUtils.getAnnotation(exchangeClass, BeanName.class);
            String beanName = name != null ? name.value()
                    : BeanDefinitionReaderUtils.generateBeanName(beanDefinition, registry);
            registry.registerBeanDefinition(beanName, beanDefinition);
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
