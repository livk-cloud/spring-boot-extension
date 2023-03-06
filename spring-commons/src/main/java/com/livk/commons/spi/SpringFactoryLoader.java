package com.livk.commons.spi;

import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * The type Spring factory loader.
 *
 * @author livk
 */
class SpringFactoryLoader extends AbstractLoader {

    @Override
    protected <T> List<T> load(Class<T> type, ClassLoader classLoader) {
        return SpringFactoriesLoader.loadFactories(type, classLoader);
    }
}
