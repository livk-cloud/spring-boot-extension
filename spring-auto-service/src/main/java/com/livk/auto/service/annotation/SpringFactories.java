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
     * 指定spring.factories文件生成接口
     * 如当前类仅有一个接口，则可以自动推断
     *
     * @return class
     */
    Class<?> value() default Void.class;

    /**
     * 将spring.factories文件调整至aot.factories
     *
     * @return the boolean
     */
    boolean aot() default false;
}
