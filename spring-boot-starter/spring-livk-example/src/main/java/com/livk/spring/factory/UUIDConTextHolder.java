package com.livk.spring.factory;

import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * <p>
 * UUIDConTextHolder
 * </p>
 *
 * @author livk
 * @date 2023/1/2
 */
@UtilityClass
public class UUIDConTextHolder {
    private static final ThreadLocal<UUID> UUID_CONTEXT = new ThreadLocal<>();

    public UUID get() {
        return UUID_CONTEXT.get();
    }

    public void set() {
        UUID_CONTEXT.set(UUID.randomUUID());
    }

    public void remove() {
        UUID_CONTEXT.remove();
    }
}
