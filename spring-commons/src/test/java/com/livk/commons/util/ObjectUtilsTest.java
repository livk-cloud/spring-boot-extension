package com.livk.commons.util;

import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * ObjectUtilsTest
 * </p>
 *
 * @author livk
 */
class ObjectUtilsTest {

    @Test
    void testAllChecked() {
        boolean result = ObjectUtils.allChecked(StringUtils::hasText, "1", "");
        assertFalse(result);
    }

    @Test
    void testAnyChecked() {
        boolean result = ObjectUtils.anyChecked(StringUtils::hasText, "1", "");
        assertTrue(result);
    }
}

