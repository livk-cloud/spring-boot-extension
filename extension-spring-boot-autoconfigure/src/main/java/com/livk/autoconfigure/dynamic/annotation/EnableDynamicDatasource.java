package com.livk.autoconfigure.dynamic.annotation;

import com.livk.autoconfigure.dynamic.datasource.DynamicDatasourceImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * EnableDynamicDatasource
 * </p>
 *
 * @author livk
 * @date 2022/12/2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(DynamicDatasourceImportSelector.class)
public @interface EnableDynamicDatasource {
}
