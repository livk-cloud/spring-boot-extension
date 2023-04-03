package com.livk.commons.spi.support;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author livk
 */
class Allprioritizedloader extends PrioritizedLoader {

    public Allprioritizedloader() {
        addLoader(SpringFactoryLoader.INSTANCE);
        addLoader(JdkServiceLoader.INSTANCE);
    }

    @Override
    public <T> List<T> load(Class<T> type) {
        return loaderDiscoverers.stream()
                .map(loader -> loader.load(type))
                .filter(Predicate.not(CollectionUtils::isEmpty))
                .reduce(this::concat)
                .orElse(Collections.emptyList());
    }

    private <T> List<T> concat(List<T> listStart, List<T> listEnd) {
        return Stream.concat(listStart.stream(), listEnd.stream())
                .collect(Collectors.toList());
    }
}
