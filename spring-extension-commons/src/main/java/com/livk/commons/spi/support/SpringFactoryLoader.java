package com.livk.commons.spi.support;

import com.livk.commons.spi.AbstractLoader;
import com.livk.commons.spi.Loader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * The type Spring factory loader.
 *
 * @author livk
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class SpringFactoryLoader extends AbstractLoader {

    static final Loader INSTANCE = new SpringFactoryLoader();

    @Override
    protected <T> List<T> load(Class<T> type, ClassLoader classLoader) {
        return SpringFactoriesLoader.loadFactories(type, classLoader);
    }
}
