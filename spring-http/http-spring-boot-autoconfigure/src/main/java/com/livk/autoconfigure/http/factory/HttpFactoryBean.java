package com.livk.autoconfigure.http.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * <p>
 * HttpFactoryBean
 * </p>
 *
 * @author livk
 * @date 2022/11/9
 */
@RequiredArgsConstructor
public class HttpFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> httpInterface;

    private final HttpServiceProxyFactory proxyFactory;

    @Override
    public T getObject() {
        return proxyFactory.createClient(httpInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return httpInterface;
    }

    public Class<T> getHttpInterface() {
        return httpInterface;
    }

    public HttpServiceProxyFactory getProxyFactory() {
        return proxyFactory;
    }
}
