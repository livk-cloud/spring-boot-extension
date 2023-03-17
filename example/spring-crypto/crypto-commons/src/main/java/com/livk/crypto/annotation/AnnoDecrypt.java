package com.livk.crypto.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.livk.crypto.CryptoType;
import com.livk.crypto.jackson.CryptoJsonDeserializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Anno decrypt.
 *
 * @author livk
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonDeserialize(using = CryptoJsonDeserializer.class)
public @interface AnnoDecrypt {

    /**
     * Value crypto type.
     *
     * @return the crypto type
     */
    CryptoType value();
}
