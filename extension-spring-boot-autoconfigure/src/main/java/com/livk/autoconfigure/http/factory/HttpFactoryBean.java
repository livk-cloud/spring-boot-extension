package com.livk.autoconfigure.http.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * <p>
 * HttpFactoryBean
 * </p>
 *
 * @param <T> the type parameter
 * @author livk
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
}
