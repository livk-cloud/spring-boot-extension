package com.livk.commons.bean;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class WrapperTest {

    @Test
    void test() {
        Wrapper<String> wrapper = Wrapper.of("livk");
        assertEquals("livk", wrapper.unwrap());
        assertTrue(wrapper instanceof Proxy);
    }
}
