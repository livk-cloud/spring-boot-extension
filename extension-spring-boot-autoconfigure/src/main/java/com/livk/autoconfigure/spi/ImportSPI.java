package com.livk.autoconfigure.spi;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * ImportSPI
 * </p>
 *
 * @author livk
 * @date 2022/12/9
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(SPIBeanRegistrar.class)
public @interface ImportSPI {

    Class<?> value();
}
