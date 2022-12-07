package com.livk.commons.util;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;


/**
 * <p>
 * AnnotationUtilsTest
 * </p>
 *
 * @author livk
 * @date 2022/12/7
 */
class AnnotationUtilsTest {

    private final Method method = AnnotationTestClass.class.getDeclaredMethod("parseMethod", String.class);

    private final MethodParameter methodParameter = new MethodParameter(method, 0);

    AnnotationUtilsTest() throws NoSuchMethodException {
    }

    @Test
    void getAnnotationElement() {
        assertNotNull(AnnotationUtils.getAnnotationElement(methodParameter, RequestMapping.class));
        assertNotNull(AnnotationUtils.getAnnotationElement(methodParameter, Controller.class));
        assertNotNull(AnnotationUtils.getAnnotationElement(methodParameter, RestController.class));
        assertNull(AnnotationUtils.getAnnotationElement(methodParameter, RequestBody.class));

        assertNotNull(AnnotationUtils.getAnnotationElement(method, RequestMapping.class));
        assertNotNull(AnnotationUtils.getAnnotationElement(method, Controller.class));
        assertNotNull(AnnotationUtils.getAnnotationElement(method, RestController.class));
        assertNull(AnnotationUtils.getAnnotationElement(method, RequestBody.class));
    }

    @Test
    void hasAnnotationElement() {
        assertTrue(AnnotationUtils.hasAnnotationElement(methodParameter, RequestMapping.class));
        assertTrue(AnnotationUtils.hasAnnotationElement(methodParameter, Controller.class));
        assertTrue(AnnotationUtils.hasAnnotationElement(methodParameter, RestController.class));
        assertFalse(AnnotationUtils.hasAnnotationElement(methodParameter, RequestBody.class));

        assertTrue(AnnotationUtils.hasAnnotationElement(method, RequestMapping.class));
        assertTrue(AnnotationUtils.hasAnnotationElement(method, Controller.class));
        assertTrue(AnnotationUtils.hasAnnotationElement(method, RestController.class));
        assertFalse(AnnotationUtils.hasAnnotationElement(method, RequestBody.class));
    }
}

@RestController
class AnnotationTestClass {

    @RequestMapping
    @SuppressWarnings("unused")
    private void parseMethod(String username) {
    }
}
