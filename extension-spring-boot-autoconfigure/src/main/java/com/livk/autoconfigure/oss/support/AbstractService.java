package com.livk.autoconfigure.oss.support;

import com.livk.autoconfigure.oss.OSSProperties;
import com.livk.autoconfigure.oss.client.OSSClientFactory;

public abstract non-sealed class AbstractService<T> implements OSSOperations {

    protected T client;

    protected AbstractService(OSSProperties properties) {
        OSSClientFactoryPatternResolver resolver = new OSSClientFactoryPatternResolver();
        OSSClientFactory<T> factory = resolver.loader(properties.getPrefix());
        this.client = factory.instance(properties.endpoint(), properties.getAccessKey(), properties.getSecretKey());
    }
}
