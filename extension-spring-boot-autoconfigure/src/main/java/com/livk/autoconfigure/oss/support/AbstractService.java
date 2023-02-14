package com.livk.autoconfigure.oss.support;

import com.livk.autoconfigure.oss.OSSProperties;
import com.livk.autoconfigure.oss.client.OSSClientFactory;

/**
 * The type Abstract oss service.
 *
 * @param <T> the type parameter
 * @author livk
 */
public abstract non-sealed class AbstractService<T> implements OSSOperations {

    /**
     * The Client.
     */
    protected T client;

    /**
     * Instantiates a new Abstract oss service.
     *
     * @param properties    the properties
     * @param clientFactory the client factory
     */
    protected AbstractService(OSSProperties properties,
                              OSSClientFactory<T> clientFactory) {
        this.client = clientFactory.instance(properties.endpoint(), properties.getAccessKey(), properties.getSecretKey());
    }
}
