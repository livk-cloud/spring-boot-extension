package com.livk.autoconfigure.oss.client;

/**
 * @author livk
 */
public interface OSSClientFactory<T> {

    T instance(String endpoint, String accessKey, String secretKey);
}
