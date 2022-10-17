package com.livk.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <p>
 * ClassUtilsTest
 * </p>
 *
 * @author livk
 * @date 2022/10/17
 */
class ClassUtilsTest {

    @Test
    void toClassTest() {
        Class<String> result = ClassUtils.toClass(String.class);
        Assertions.assertEquals(String.class, result);
    }
}
