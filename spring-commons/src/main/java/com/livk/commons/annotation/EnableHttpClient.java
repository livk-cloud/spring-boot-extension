package com.livk.commons.annotation;

import com.livk.commons.http.RestTemplateImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * EnableHttpClient
 * </p>
 *
 * @author livk
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RestTemplateImportSelector.class)
public @interface EnableHttpClient {

}
