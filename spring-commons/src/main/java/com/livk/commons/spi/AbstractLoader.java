package com.livk.commons.spi;

import java.util.List;

/**
 * @author livk
 */
public abstract class AbstractLoader implements Loader {
    @Override
    public <T> List<T> load(Class<T> type) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return load(type, classLoader);
    }

    protected abstract <T> List<T> load(Class<T> type, ClassLoader classLoader);
}
