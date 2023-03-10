package com.livk.commons.function;

import org.junit.jupiter.api.Test;

/**
 * <p>
 * PresentTest
 * </p>
 *
 * @author livk
 */
class PresentTest {

    @Test
    void present() {
        Present.handler(1, i -> i == 1).present(System.out::println, () -> System.out.println(2));
        Present.handler(1, i -> i != 1).present(System.out::println, () -> System.out.println(2));
    }
}
