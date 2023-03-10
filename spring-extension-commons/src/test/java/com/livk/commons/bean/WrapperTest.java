package com.livk.commons.bean;

import com.livk.commons.proxy.ProxyType;
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
        Wrapper<String> jdkWrapper = Wrapper.proxy("livk", ProxyType.JDK_PROXY);
        Wrapper<String> byteWrapper = Wrapper.proxy("livk", ProxyType.BYTE_BUDDY);
        assertEquals("livk", wrapper.unwrap());
        assertEquals("livk", jdkWrapper.unwrap());
        assertEquals("livk", byteWrapper.unwrap());
        assertTrue(wrapper instanceof Proxy);
    }
}
