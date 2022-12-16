package com.livk.commons.domain;

import org.springframework.beans.factory.FactoryBean;

/**
 * <p>
 * Wrapper
 * </p>
 *
 * @author livk
 * @date 2022/12/16
 */
public record Wrapper<T>(T obj) implements FactoryBean<Wrapper<T>> {

    @Override
    public Wrapper<T> getObject() throws Exception {
        return this;
    }

    @Override
    public Class<?> getObjectType() {
        return this.getClass();
    }
}
