package com.livk.ip.config;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * IP
 * </p>
 *
 * @author livk
 * @date 2022/8/22
 */
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IPValidator.class)
public @interface IP {
    String message() default "IP不符合格式";

    boolean dns() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    interface Constant {
        String HTTPS = "https://";
        String HTTP = "http://";
    }
}
