package com.livk.commons.spi;

import java.util.List;

/**
 * The interface Loader.
 *
 * @author livk
 */
public interface Loader {

    /**
     * Load stream.
     *
     * @param <T>     the type parameter
     * @param type    the type
     * @param manager the loader type
     * @return the stream
     */
    static <T> List<T> load(Class<T> type, LoaderManager manager) {
        return manager.loader().load(type);
    }

    /**
     * Load stream.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the stream
     */
    <T> List<T> load(Class<T> type);
}
