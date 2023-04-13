package com.livk.commons.spring.context;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * The interface Auto import.
 *
 * @author livk
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AutoImportSelector.class)
public @interface AutoImport {
}
