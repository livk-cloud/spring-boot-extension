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
