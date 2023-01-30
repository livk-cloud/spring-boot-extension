package com.livk.autoconfigure.useragent.yauaa.support;

import nl.basjes.parse.useragent.UserAgent;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;

/**
 * <p>
 * UserAgentContextHolder
 * </p>
 *
 * @author livk
 */
public class UserAgentContextHolder {
    private static final ThreadLocal<UserAgent> context = new NamedThreadLocal<>("useragent context");

    private static final ThreadLocal<UserAgent> inheritableContext = new NamedInheritableThreadLocal<>("inheritable useragent context");

    /**
     * Set.
     *
     * @param userAgent the user agent
     */
    public static void set(UserAgent userAgent) {
        set(userAgent, false);
    }

    /**
     * Set.
     *
     * @param userAgent   the user agent
     * @param inheritable the inheritable
     */
    public static void set(UserAgent userAgent, boolean inheritable) {
        if (userAgent != null) {
            if (inheritable) {
                inheritableContext.set(userAgent);
                context.remove();
            } else {
                context.set(userAgent);
                inheritableContext.remove();
            }
        } else {
            remove();
        }
    }

    /**
     * Get user agent.
     *
     * @return the user agent
     */
    public static UserAgent get() {
        UserAgent userAgent = context.get();
        if (userAgent == null) {
            userAgent = inheritableContext.get();
        }
        return userAgent;
    }

    /**
     * Remove.
     */
    public static void remove() {
        context.remove();
        inheritableContext.remove();
    }
}
