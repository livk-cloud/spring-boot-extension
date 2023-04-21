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

package com.livk.commons.spi;

import com.google.auto.service.AutoService;
import com.livk.auto.service.annotation.SpringFactories;
import com.livk.commons.spi.support.UniversalManager;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class LoaderTest {

    @Test
    public void loadTest() {
        assertEquals(List.of(Dog.class), Loader.load(Animal.class, UniversalManager.SPRING_FACTORY).stream().map(Object::getClass).toList());
        assertEquals(List.of(Cat.class), Loader.load(Animal.class, UniversalManager.JDK_SERVICE).stream().map(Object::getClass).toList());
        assertEquals(Set.of(Dog.class, Cat.class), Loader.load(Animal.class, UniversalManager.ALL).stream().map(Object::getClass).collect(Collectors.toSet()));
    }

    public interface Animal {

    }

    @SpringFactories
    public static class Dog implements Animal {

    }

    @AutoService(Animal.class)
    public static class Cat implements Animal {

    }
}
