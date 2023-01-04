package com.livk.autoconfigure.mybatis.monitor;

import com.livk.autoconfigure.mybatis.MybatisLogMonitorImportSelector;
import org.springframework.context.annotation.Import;

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
 * @date 2023/1/4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MybatisLogMonitorImportSelector.class)
public @interface EnableSqlMonitor {
}
