package com.livk.crypto.annotation;

import com.livk.crypto.CryptoType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Anno decrypt.
 *
 * @author livk
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnoDecrypt {

    /**
     * Value crypto type.
     *
     * @return the crypto type
     */
    CryptoType value();
}
