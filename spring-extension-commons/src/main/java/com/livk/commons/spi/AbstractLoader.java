package com.livk.commons.spi;

import java.util.List;

/**
 * The type Abstract loader.
 *
 * @author livk
 */
public abstract class AbstractLoader implements Loader {
    @Override
    public <T> List<T> load(Class<T> type) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return load(type, classLoader);
    }

    /**
     * Load list.
     *
     * @param <T>         the type parameter
     * @param type        the type
     * @param classLoader the class loader
     * @return the list
     */
    protected abstract <T> List<T> load(Class<T> type, ClassLoader classLoader);
}
