package com.livk.annotation;

import com.livk.interceptor.SqlInterceptor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * EnableSqlPlugin
 * </p>
 *
 * @author livk
 * @date 2022/3/3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(SqlInterceptor.class)
public @interface EnableSqlPlugin {
}
