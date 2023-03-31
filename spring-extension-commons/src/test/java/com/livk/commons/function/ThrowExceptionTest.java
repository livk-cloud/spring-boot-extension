package com.livk.commons.function;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * <p>
 * ThrowExceptionTest
 * </p>
 *
 * @author livk
 */
@Slf4j
class ThrowExceptionTest {

    @Test
    void throwException() throws Throwable {
        try {
            ThrowException.isTrue(new Object(), Objects::nonNull).throwException(new RuntimeException("xxx"));
        } catch (Throwable e) {
            log.warn("exception:{}", e.getMessage());
        }
        ThrowException.isTrue(new Object(), Objects::isNull).throwException(RuntimeException::new);
    }
}
