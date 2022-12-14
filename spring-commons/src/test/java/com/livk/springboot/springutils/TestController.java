package com.livk.springboot.springutils;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * TestController
 * </p>
 *
 * @author livk
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@TestAnnotation
@Controller
public @interface TestController {

    @AliasFor(annotation = Controller.class)
    String value() default "";
}
