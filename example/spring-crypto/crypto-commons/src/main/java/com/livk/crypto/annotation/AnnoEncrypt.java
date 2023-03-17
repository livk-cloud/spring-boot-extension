package com.livk.crypto.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.livk.crypto.CryptoType;
import com.livk.crypto.jackson.CryptoJsonSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author livk
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = CryptoJsonSerializer.class)
public @interface AnnoEncrypt {
    /**
     * Value crypto type.
     *
     * @return the crypto type
     */
    CryptoType value();
}
