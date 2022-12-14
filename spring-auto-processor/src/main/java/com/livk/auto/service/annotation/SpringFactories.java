package com.livk.auto.service.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * SpringFactories
 * </p>
 *
 * @author livk
 * @date 2022/12/13
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface SpringFactories {

    Class<?> type();
}
