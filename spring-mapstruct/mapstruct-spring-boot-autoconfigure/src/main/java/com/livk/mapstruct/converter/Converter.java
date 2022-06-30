package com.livk.mapstruct.converter;

import java.util.Collection;
import java.util.stream.Stream;

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

    Stream<S> streamSource(Collection<T> t);

    Stream<T> streamTarget(Collection<S> s);

}
