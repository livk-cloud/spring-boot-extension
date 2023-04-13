package com.livk.selector;

import com.livk.commons.spring.context.AutoImport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * LivkImport
 * </p>
 *
 * @author livk
 */
@AutoImport
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LivkImport {

}
