package com.livk.autoconfigure.useragent.servlet;

import com.livk.commons.domain.Wrapper;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;

/**
 * The type User agent context holder.
 */
public class UserAgentContextHolder {

    private static final ThreadLocal<Wrapper<?>> context = new NamedThreadLocal<>("useragent context");

    private static final ThreadLocal<Wrapper<?>> inheritableContext = new NamedInheritableThreadLocal<>("inheritable useragent context");

    /**
     * Sets user agent context.
     *
     * @param useragentWrapper the useragent wrapper
     * @param inheritable      the inheritable
     */
    public static void setUserAgentContext(Wrapper<?> useragentWrapper, boolean inheritable) {
        if (useragentWrapper != null) {
            if (inheritable) {
                inheritableContext.set(useragentWrapper);
                context.remove();
            } else {
                context.set(useragentWrapper);
                inheritableContext.remove();
            }
        } else {
            cleanUserAgentContext();
        }
    }

    /**
     * Gets user agent context.
     *
     * @return the user agent context
     */
    public static Wrapper<?> getUserAgentContext() {
        Wrapper<?> useragentWrapper = context.get();
        if (useragentWrapper == null) {
            useragentWrapper = inheritableContext.get();
        }
        return useragentWrapper;
    }

    /**
     * Sets user agent context.
     *
     * @param useragentWrapper the useragent wrapper
     */
    public static void setUserAgentContext(Wrapper<?> useragentWrapper) {
        setUserAgentContext(useragentWrapper, false);
    }

    /**
     * Clean user agent context.
     */
    public static void cleanUserAgentContext() {
        context.remove();
        inheritableContext.remove();
    }
}
