package com.livk.commons.spi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The enum Loader type.
 */
@Getter
@RequiredArgsConstructor
public enum LoaderType {

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
            for (LoaderType loaderType : values()) {
                if (loaderType == NONE || loaderType == ALL) {
                    continue;
                }
                list.addAll(Loader.load(type, loaderType));
            }
            return list;
        }
    });

    private final Loader loader;
}
