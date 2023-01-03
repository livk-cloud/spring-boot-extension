package com.livk.commons.util;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

/**
 * <p>
 * ReflectionUtilsTest
 * </p>
 *
 * @author livk
 */
class ReflectionUtilsTest {

    final Field fieldNo = Maker.class.getDeclaredField("no");

    ReflectionUtilsTest() throws NoSuchFieldException {
    }

    @Test
    void getFieldNameTest() {
        String result = ReflectionUtils.getFieldName(Maker::getNo);
        assertEquals("no", result);
    }

    @Test
    void setFieldAndAccessibleTest() {
        Maker maker = new Maker();
        ReflectionUtils.setFieldAndAccessible(fieldNo, maker, 2);
        assertEquals(2, maker.getNo());
    }

    @Test
    void getReadMethods() {
        Set<Method> readMethods = ReflectionUtils.getReadMethods(Maker.class);
        List<String> list = readMethods.stream().map(Method::getName).collect(Collectors.toList());
        assertLinesMatch(List.of("getNo", "getUsername"), list);
    }

    @Test
    void getReadMethod() {
        Method readMethod = ReflectionUtils.getReadMethod(Maker.class, fieldNo);
        assertEquals("getNo", readMethod.getName());
    }

    @Test
    void getAllFields() {
        List<Field> fieldList = ReflectionUtils.getAllFields(Maker.class);
        List<String> list = fieldList.stream().map(Field::getName).collect(Collectors.toList());
        assertLinesMatch(List.of("no", "username"), list);
    }
}

@Data
class Maker {
    private Integer no;

    private String username;
}
