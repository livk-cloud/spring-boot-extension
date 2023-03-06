package com.livk.autoconfigure.oss.support;

import com.livk.autoconfigure.oss.OSSProperties;
import com.livk.autoconfigure.oss.client.OSSClientFactory;

/**
 * The type Abstract service.
 *
 * @param <T> the type parameter
 */
public abstract non-sealed class AbstractService<T> implements OSSOperations {

    /**
     * The Client.
     */
    protected T client;

    /**
     * Instantiates a new Abstract service.
     *
     * @param properties the properties
     */
    protected AbstractService(OSSProperties properties) {
        OSSClientFactoryPatternResolver resolver = new OSSClientFactoryPatternResolver();
        OSSClientFactory<T> factory = resolver.loader(properties.getPrefix());
        this.client = factory.instance(properties.endpoint(), properties.getAccessKey(), properties.getSecretKey());
    }
}
