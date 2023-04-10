package com.livk.autoconfigure.limit.annotation;

import com.livk.autoconfigure.limit.LimitImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author livk
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(LimitImportSelector.class)
public @interface EnableLimit {


}
