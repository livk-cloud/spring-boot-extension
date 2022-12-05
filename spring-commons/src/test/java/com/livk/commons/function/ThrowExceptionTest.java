package com.livk.commons.function;

import com.livk.commons.util.LogUtils;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * <p>
 * ThrowExceptionTest
 * </p>
 *
 * @author livk
 */
class ThrowExceptionTest {

    @Test
    void throwException() throws Throwable {
        try {
            ThrowException.isTrue(new Object(), Objects::nonNull).throwException(new RuntimeException("xxx"));
        } catch (Throwable e) {
            LogUtils.warn("exception:{}", e.getMessage());
        }
        ThrowException.isTrue(new Object(), Objects::isNull).throwException(RuntimeException::new);
    }
}
