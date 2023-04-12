package com.livk.commons.spring.context;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
@SpringBootTest
class AnnotationMetadataResolverTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private BeanFactory beanFactory;

    @Test
    void find() {
        AnnotationMetadataResolver resolver = new AnnotationMetadataResolver(resourceLoader);

        assertEquals(Set.of(A.class, TestController.class),
                resolver.find(TestAnnotation.class, "com.livk.commons.spring.context"));

        assertEquals(Set.of(A.class, TestController.class),
                resolver.find(TestAnnotation.class, beanFactory));
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation {
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @TestAnnotation
    @Controller
    @interface TestController {

        @AliasFor(annotation = Controller.class)
        String value() default "";
    }

    @TestController
    static class A {
    }
}
