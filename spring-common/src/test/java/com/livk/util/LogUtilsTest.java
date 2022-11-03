package com.livk.util;

import org.junit.jupiter.api.Test;

/**
 * <p>
 * LogUtilsTest
 * </p>
 *
 * @author livk
 * @date 2022/10/12
 */
class LogUtilsTest {

    @Test
    void infoTest() {
        LogUtils.info("666");
    }

    @Test
    void traceTest() {
        LogUtils.trace("666");
    }

    @Test
    void warnTest() {
        LogUtils.warn("666");
    }

    @Test
    void debugTest() {
        LogUtils.debug("666");
    }

    @Test
    void errorTest() {
        LogUtils.error("666");
    }
}
