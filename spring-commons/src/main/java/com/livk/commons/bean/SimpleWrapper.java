package com.livk.commons.bean;

import lombok.RequiredArgsConstructor;

/**
 * The type Simple wrapper.
 *
 * @param <T> the type parameter
 * @author livk
 */
@RequiredArgsConstructor
public class SimpleWrapper<T> implements Wrapper<T> {

    private final T t;

    @Override
    public T unwrap() {
        return t;
    }
}
