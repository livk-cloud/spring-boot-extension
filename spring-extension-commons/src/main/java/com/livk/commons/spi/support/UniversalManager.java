package com.livk.commons.spi.support;

import com.livk.commons.spi.Loader;
import com.livk.commons.spi.LoaderManager;
import lombok.RequiredArgsConstructor;

/**
 * The enum Loader type.
 */
@RequiredArgsConstructor
public enum UniversalManager implements LoaderManager {

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
    public Loader loader() {
        return this.loader;
    }
}
