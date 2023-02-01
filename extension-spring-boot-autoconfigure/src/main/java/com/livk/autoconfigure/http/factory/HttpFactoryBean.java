package com.livk.autoconfigure.http.factory;

import lombok.Setter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * <p>
 * HttpFactoryBean
 * </p>
 *
 * @param <T> the type parameter
 * @author livk
 */
@Setter
public class HttpFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware, BeanFactoryAware {

    private Class<T> httpInterfaceType;

    private ApplicationContext applicationContext;

    private BeanFactory beanFactory;

    @Override
    public T getObject() {
        HttpServiceProxyFactory proxyFactory = beanFactory != null ? beanFactory.getBean(HttpServiceProxyFactory.class)
                : applicationContext.getBean(HttpServiceProxyFactory.class);
        return proxyFactory.createClient(httpInterfaceType);
    }

    @Override
    public Class<?> getObjectType() {
        return httpInterfaceType;
    }
}
