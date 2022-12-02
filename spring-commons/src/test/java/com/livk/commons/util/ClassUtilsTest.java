package com.livk.commons.util;

import com.livk.commons.util.ClassUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(String.class, result);
    }
}
