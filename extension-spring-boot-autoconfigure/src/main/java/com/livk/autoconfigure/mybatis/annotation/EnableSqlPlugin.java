package com.livk.autoconfigure.mybatis.annotation;

import com.livk.autoconfigure.mybatis.InterceptorImportSelector;
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
@Import(InterceptorImportSelector.class)
public @interface EnableSqlPlugin {

}
