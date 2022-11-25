package com.livk.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

/**
 * <p>
 * ObjectUtilsTest
 * </p>
 *
 * @author livk
 * @date 2022/7/6
 */
class ObjectUtilsTest {

    @Test
    void testAllChecked() {
        boolean result = ObjectUtils.allChecked(StringUtils::hasText, "1", "");
        Assertions.assertFalse(result);
    }

    @Test
    void testAnyChecked() {
        boolean result = ObjectUtils.anyChecked(StringUtils::hasText, "1", "");
        Assertions.assertTrue(result);
    }
}

