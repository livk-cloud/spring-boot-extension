package com.livk.mapstruct.annotation;

import com.livk.mapstruct.MapstructAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * EnableConverter
 * </p>
 *
 * @author livk
 * @date 2022/5/11
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MapstructAutoConfiguration.class)
public @interface EnableConverter {

}
