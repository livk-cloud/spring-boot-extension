package com.livk.spring.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;

import java.io.Serializable;

/**
 * <p>
 * {@see AutowireUtils#resolveAutowiringValue(Object, Class)}
 * 需要注册interface并且当前类实现{@link Serializable}
 * 则会被spring代理
 * </p>
 *
 * @author livk
 * @date 2023/1/2
 */
public class UUIDObjectFactory implements ObjectFactory<UUIDRequest>, Serializable {

    @Override
    public UUIDRequest getObject() throws BeansException {
        return UUIDConTextHolder::get;
    }
}
