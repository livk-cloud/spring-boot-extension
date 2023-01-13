package com.livk.autoconfigure.mapstruct.converter;

/**
 * <p>
 * Converter
 * </p>
 *
 * @param <S> the type parameter
 * @param <T> the type parameter
 * @author livk
 */
public interface Converter<S, T> {

    /**
     * Gets source.
     *
     * @param t the t
     * @return the source
     */
    S getSource(T t);

    /**
     * Gets target.
     *
     * @param s the s
     * @return the target
     */
    T getTarget(S s);

}
