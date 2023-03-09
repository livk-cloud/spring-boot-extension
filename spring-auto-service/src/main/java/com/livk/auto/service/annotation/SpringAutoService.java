package com.livk.auto.service.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * SpringAutoService
 * </p>
 *
 * @author livk
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface SpringAutoService {
    /**
     * Annotation for automatic assembly, default org.springframework.boot.autoconfigure.AutoConfiguration
     *
     * @return class
     */
    Class<? extends Annotation> auto() default Annotation.class;
}
