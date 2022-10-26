package com.livk.autoconfigure.mapstruct.converter;

/**
 * <p>
 * Converter
 * </p>
 *
 * @author livk
 * @date 2022/5/11
 */
public interface Converter<S, T> {

    S getSource(T t);

    T getTarget(S s);

}
