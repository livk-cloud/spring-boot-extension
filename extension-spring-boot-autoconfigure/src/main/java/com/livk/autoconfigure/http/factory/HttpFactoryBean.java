package com.livk.autoconfigure.http.factory;

import lombok.Setter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * <p>
 * HttpFactoryBean
 * </p>
 *
 * @author livk
 */
@Setter
public class HttpFactoryBean implements FactoryBean<Object> {

    private Class<?> httpInterfaceType;

    private BeanFactory beanFactory;

    @Override
    public Object getObject() {
        HttpServiceProxyFactory proxyFactory = beanFactory.getBean(HttpServiceProxyFactory.class);
        return proxyFactory.createClient(httpInterfaceType);
    }

    @Override
    public Class<?> getObjectType() {
        return httpInterfaceType;
    }
}
