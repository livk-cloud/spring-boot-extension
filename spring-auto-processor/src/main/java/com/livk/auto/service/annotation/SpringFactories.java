package com.livk.auto.service.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * SpringFactories
 * </p>
 *
 * @author livk
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface SpringFactories {
    /**
     * spring factories process interface
     *
     * @return class
     */
    Class<?> type();
}
