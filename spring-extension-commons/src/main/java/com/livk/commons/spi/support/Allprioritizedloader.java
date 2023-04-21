/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
