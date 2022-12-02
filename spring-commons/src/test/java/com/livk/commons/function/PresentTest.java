package com.livk.commons.function;

import com.livk.commons.function.Present;
import org.junit.jupiter.api.Test;

/**
 * <p>
 * PresentTest
 * </p>
 *
 * @author livk
 * @date 2022/10/17
 */
class PresentTest {

    @Test
    void present() {
        Present.handler(1, i -> i == 1).present(System.out::println, () -> System.out.println(2));
        Present.handler(1, i -> i != 1).present(System.out::println, () -> System.out.println(2));
    }
}
