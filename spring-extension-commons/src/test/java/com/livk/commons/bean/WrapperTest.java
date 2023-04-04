package com.livk.commons.bean;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class WrapperTest {

    @Test
    void test() {
        String value = "livk";
        GenericWrapper<String> wrapper = GenericWrapper.of(value);
        assertEquals(value, wrapper.unwrap(String.class));
        assertEquals(value, wrapper.unwrap());
        assertTrue(wrapper.isWrapperFor(String.class));
        assertEquals(value, Wrapper.tryUnwrap(wrapper, String.class));
    }
}
