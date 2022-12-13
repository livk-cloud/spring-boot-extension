package com.livk.auto.service.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * SpringAutoService
 * </p>
 *
 * @author livk
 * @date 2022/12/13
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface SpringAutoService {

    Class<? extends Annotation> auto() default Annotation.class;
}
