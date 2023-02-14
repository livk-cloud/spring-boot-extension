package com.livk.autoconfigure.oss.client;

/**
 * The interface Oss client factory.
 *
 * @param <T> the type parameter
 * @author livk
 */
public interface OSSClientFactory<T> {

    /**
     * Instance t.
     *
     * @param endpoint  the endpoint
     * @param accessKey the access key
     * @param secretKey the secret key
     * @return the t
     */
    T instance(String endpoint, String accessKey, String secretKey);
}
