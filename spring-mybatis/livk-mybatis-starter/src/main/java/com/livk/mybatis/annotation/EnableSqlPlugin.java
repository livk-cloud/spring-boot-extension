package com.livk.mybatis.annotation;

import com.livk.mybatis.InterceptorAutoConfiguration;
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
@Import(InterceptorAutoConfiguration.class)
public @interface EnableSqlPlugin {

}
