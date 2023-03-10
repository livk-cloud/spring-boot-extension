package com.livk.commons.test;

import org.junit.jupiter.api.Test;

/**
 * <p>
 * TestLogUtilsTest
 * </p>
 *
 * @author livk
 */
class TestLogUtilsTest {

    @Test
    void infoTest() {
        TestLogUtils.info("666");
    }

    @Test
    void traceTest() {
        TestLogUtils.trace("666");
    }

    @Test
    void warnTest() {
        TestLogUtils.warn("666");
    }

    @Test
    void debugTest() {
        TestLogUtils.debug("666");
    }

    @Test
    void errorTest() {
        TestLogUtils.error("666");
    }
}
