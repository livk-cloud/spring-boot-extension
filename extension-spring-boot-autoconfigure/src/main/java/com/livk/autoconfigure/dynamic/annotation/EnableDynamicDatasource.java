package com.livk.autoconfigure.dynamic.annotation;

import com.livk.autoconfigure.dynamic.DynamicDatasourceImportSelector;
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
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(DynamicDatasourceImportSelector.class)
public @interface EnableDynamicDatasource {
}
