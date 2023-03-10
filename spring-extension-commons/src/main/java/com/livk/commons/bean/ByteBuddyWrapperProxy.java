package com.livk.commons.bean;

import com.livk.commons.bean.util.BeanUtils;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author livk
 */
@RequiredArgsConstructor
class ByteBuddyWrapperProxy<T> implements Wrapper<Wrapper<T>> {

    private final WrapperProxy<T> proxy;

    @SuppressWarnings("unchecked")
    @Override
    public Wrapper<T> unwrap() {
        Class<? extends Wrapper<T>> wrapperClass = (Class<? extends Wrapper<T>>) new ByteBuddy()
                .subclass(Wrapper.class)
                .method(ElementMatchers.named("unwrap"))
                .intercept(InvocationHandlerAdapter.of(proxy))
                .make()
                .load(Thread.currentThread().getContextClassLoader())
                .getLoaded();
        return BeanUtils.instantiateClass(wrapperClass);
    }
}
