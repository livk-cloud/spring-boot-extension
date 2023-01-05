package com.livk.commons.util;

import org.junit.jupiter.api.Test;

/**
 * <p>
 * LogUtilsTest
 * </p>
 *
 * @author livk
 */
class LogUtilsTest {

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
