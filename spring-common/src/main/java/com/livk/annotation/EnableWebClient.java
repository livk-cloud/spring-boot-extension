package com.livk.annotation;

import com.livk.http.WebClientConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * EnableWebClient
 * </p>
 *
 * @author livk
 * @date 2022/5/9
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(WebClientConfig.class)
public @interface EnableWebClient {
}
