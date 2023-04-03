package com.livk.commons.spi.support;

import com.google.common.collect.Lists;
import com.livk.commons.spi.AbstractLoader;
import com.livk.commons.spi.Loader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ServiceLoader;

/**
 * The type Jdk service loader.
 *
 * @author livk
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class JdkServiceLoader extends AbstractLoader {

    static final Loader INSTANCE = new JdkServiceLoader();

    @Override
    protected <T> List<T> load(Class<T> type, ClassLoader classLoader) {
        return Lists.newArrayList(ServiceLoader.load(type, classLoader));
    }
}
