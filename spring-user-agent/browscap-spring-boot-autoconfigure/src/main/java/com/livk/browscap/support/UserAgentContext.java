package com.livk.browscap.support;

import com.blueconic.browscap.Capabilities;

/**
 * <p>
 * UserAgentContext
 * </p>
 *
 * @author livk
 * @date 2022/9/30
 */
public class UserAgentContext {
    private static final ThreadLocal<Capabilities> context = new ThreadLocal<>();

    public static void set(Capabilities capabilities) {
        context.set(capabilities);
    }

    public static Capabilities get() {
        return context.get();
    }

    public static void remove() {
        context.remove();
    }
}
