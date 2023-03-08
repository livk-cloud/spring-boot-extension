package com.livk.commons.spi;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The enum Loader type.
 */
@RequiredArgsConstructor
public enum DefaultLoaderType implements LoaderType {

    /**
     * The None.
     */
    NONE(new Loader() {
        @Override
        public <T> List<T> load(Class<T> type) {
            return Collections.emptyList();
        }
    }),

    /**
     * The Spring factory.
     */
    SPRING_FACTORY(new SpringFactoryLoader()),

    /**
     * The Jdk service.
     */
    JDK_SERVICE(new JdkServiceLoader()),

    /**
     * The All.
     */
    ALL(new Loader() {
        @Override
        public <T> List<T> load(Class<T> type) {
            List<T> list = new ArrayList<>();
            for (DefaultLoaderType loaderType : values()) {
                if (loaderType == NONE || loaderType == ALL) {
                    continue;
                }
                list.addAll(loaderType.load(type));
            }
            return list;
        }
    });

    private final Loader loader;

    @Override
    public <T> List<T> load(Class<T> type) {
        return this.loader.load(type);
    }
}
