package com.livk.selector;

import org.springframework.context.annotation.Import;

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
 * @date 2022/9/7
 */
@Import(LivkImportSelector.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LivkImport {

}
