package com.livk.commons.spi.support;

import com.google.common.collect.Lists;
import com.livk.commons.spi.AbstractLoader;

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
