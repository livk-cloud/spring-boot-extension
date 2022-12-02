package com.livk.commons.util;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * ReflectionUtilsTest
 * </p>
 *
 * @author livk
 * @date 2022/10/17
 */
class ReflectionUtilsTest {

    final Field fieldNo = Maker.class.getDeclaredField("no");

    ReflectionUtilsTest() throws NoSuchFieldException {
    }

    @Test
    void set() throws IllegalAccessException {
        Maker maker = new Maker();
        ReflectionUtils.set(fieldNo, maker, 2);
        assertEquals(2, maker.getNo());
    }
}

@Data
class Maker {
    private Integer no;
}
