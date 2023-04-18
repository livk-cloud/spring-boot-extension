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
