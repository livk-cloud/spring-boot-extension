package com.livk.autoconfigure.ip2region.annotation;

import com.livk.autoconfigure.ip2region.IPValidator;
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
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IPValidator.class)
public @interface IP {
    /**
     * Message string.
     *
     * @return the string
     */
    String message() default "IP不符合格式";

    /**
     * Dns boolean.
     *
     * @return the boolean
     */
    boolean dns() default true;

    /**
     * Groups class [ ].
     *
     * @return the class [ ]
     */
    Class<?>[] groups() default {};

    /**
     * Payload class [ ].
     *
     * @return the class [ ]
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * The interface Constant.
     */
    interface Constant {
        /**
         * The constant HTTPS.
         */
        String HTTPS = "https://";
        /**
         * The constant HTTP.
         */
        String HTTP = "http://";
    }
}
