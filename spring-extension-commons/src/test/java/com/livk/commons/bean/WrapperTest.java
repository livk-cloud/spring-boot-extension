package com.livk.commons.bean;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class WrapperTest {

    @Test
    void test() {
        String value = "livk";
        Wrapper<String> wrapper = Wrapper.of(value);
        assertEquals(value, wrapper.unwrap());
    }
}
