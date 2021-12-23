package com.livk.starter01;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>
 * EnableLivk{@link LivkDemo}
 * 将此注解标注在被Spring托管的类上，即可将LivkDemo.class注入Spring ioc
 * </p>
 *
 * @author livk
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import(LivkDemo.class)
public @interface EnableLivk {

}
