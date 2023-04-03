package com.livk.commons.spi.support;

import com.livk.commons.spi.Loader;
import com.livk.commons.spi.LoaderType;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * The enum Loader type.
 */
@RequiredArgsConstructor
public enum UniversalLoaderType implements LoaderType {

    /**
     * The Spring factory.
     */
    SPRING_FACTORY(SpringFactoryLoader.INSTANCE),

    /**
     * The Jdk service.
     */
    JDK_SERVICE(JdkServiceLoader.INSTANCE),

    /**
     * The All.
     */
    ALL(new Allprioritizedloader());

    private final Loader loader;

    @Override
    public <T> List<T> load(Class<T> type) {
        return this.loader.load(type);
    }
}
