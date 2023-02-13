package com.livk.autoconfigure.oss.support;

import com.livk.autoconfigure.oss.OSSProperties;

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
     * @param properties the properties
     */
    protected AbstractService(OSSProperties properties) {
        this.client = this.instance(properties);
    }

    /**
     * Instance t.
     *
     * @param properties the properties
     * @return the t
     */
    protected abstract T instance(OSSProperties properties);
}
