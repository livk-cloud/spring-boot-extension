package com.livk.autoconfigure.mapstruct.converter;

/**
 * <p>
 * Converter
 * </p>
 *
 * @author livk
 *
 */
public interface Converter<S, T> {

    S getSource(T t);

    T getTarget(S s);

}
