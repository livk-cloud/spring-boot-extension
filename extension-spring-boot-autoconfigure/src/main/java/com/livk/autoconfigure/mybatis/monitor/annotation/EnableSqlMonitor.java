package com.livk.autoconfigure.mybatis.monitor.annotation;

import com.livk.commons.spring.context.AutoImport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * EnableSqlMonitor
 * </p>
 *
 * @author livk
 */
@AutoImport
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableSqlMonitor {
}
