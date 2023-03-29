package com.livk.commons.function;

import com.livk.commons.bean.domain.Pair;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class FieldFuncTest {

    final Field field1 = Maker.class.getDeclaredField("no");
    final Field field2 = Maker.class.getDeclaredField("username");

    FieldFuncTest() throws NoSuchFieldException {
    }

    @Test
    void get() {
        assertEquals("key", FieldFunc.<Pair<String, String>>getName(Pair::key));
        assertEquals("value", FieldFunc.<Pair<String, String>>getName(Pair::value));
        assertEquals(field1.getName(), FieldFunc.getName(Maker::getNo));
        assertEquals(field2.getName(), FieldFunc.getName(Maker::getUsername));
    }

    @Test
    void getField() {
        assertEquals(field1, FieldFunc.get(Maker::getNo));
        assertEquals(field2, FieldFunc.get(Maker::getUsername));
    }

    @Data
    static class Maker {
        private Integer no;

        private String username;
    }
}
