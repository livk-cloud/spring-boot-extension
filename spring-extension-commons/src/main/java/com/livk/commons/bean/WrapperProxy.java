package com.livk.commons.bean;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * The type Wrapper proxy.
 *
 * @param <T> the type parameter
 * @author livk
 */
@RequiredArgsConstructor
class WrapperProxy<T> implements InvocationHandler, Wrapper<Wrapper<T>> {

    private final T t;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        return method.invoke((Wrapper<T>) () -> t, args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Wrapper<T> unwrap() {
        return (Wrapper<T>) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[]{Wrapper.class}, this);
    }
}
