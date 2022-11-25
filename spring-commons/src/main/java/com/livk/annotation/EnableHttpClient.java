package com.livk.annotation;

import com.livk.http.RestTemplateImportSelector;
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
 * @date 2022/6/7
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RestTemplateImportSelector.class)
public @interface EnableHttpClient {

}
