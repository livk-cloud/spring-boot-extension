package com.livk.autoconfigure.dynamic.annotation;

import com.livk.commons.spring.context.AutoImport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * EnableDynamicDatasource
 * </p>
 *
 * @author livk
 */
@AutoImport
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EnableDynamicDatasource {
}
