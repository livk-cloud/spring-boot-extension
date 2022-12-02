package com.livk.autoconfigure.useragent.browscap.support;

import com.blueconic.browscap.Capabilities;

/**
 * <p>
 * UserAgentContextHolder
 * </p>
 *
 * @author livk
 * @date 2022/9/30
 */
public class UserAgentContextHolder {
    private static final ThreadLocal<Capabilities> context = new ThreadLocal<>();

    private static final ThreadLocal<Capabilities> inheritableContext = new InheritableThreadLocal<>();

    public static void set(Capabilities capabilities) {
        context.set(capabilities);
        inheritableContext.set(capabilities);
    }

    public static Capabilities get() {
        Capabilities capabilities = context.get();
        if (capabilities == null) {
            capabilities = inheritableContext.get();
        }
        return capabilities;
    }

    public static void remove() {
        context.remove();
        inheritableContext.remove();
    }
}
