package com.livk.starter01;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * <p>
 * LivkLivkComponent
 * </p>
 *
 * @author livk
 * @date 2021/10/18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface LivkComponent {

	@AliasFor(annotation = Component.class)
	String value() default "";

}
