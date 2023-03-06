package com.livk.commons.spi;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.ServiceLoader;

/**
 * The type Jdk service loader.
 *
 * @author livk
 */
class JdkServiceLoader extends AbstractLoader {

    @Override
    protected <T> List<T> load(Class<T> type, ClassLoader classLoader) {
        return Lists.newArrayList(ServiceLoader.load(type, classLoader));
    }
}
