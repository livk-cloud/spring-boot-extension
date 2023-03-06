package com.livk.autoconfigure.oss.client;

/**
 * The interface Oss client factory loader.
 *
 * @author livk
 */
public interface OSSClientFactoryLoader {

    /**
     * Loader oss client factory.
     *
     * @param <T>    the type parameter
     * @param prefix the prefix
     * @return the oss client factory
     */
    <T> OSSClientFactory<T> loader(String prefix);
}
