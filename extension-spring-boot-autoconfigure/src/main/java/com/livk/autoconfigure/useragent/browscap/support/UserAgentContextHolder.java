package com.livk.autoconfigure.useragent.browscap.support;

import com.blueconic.browscap.Capabilities;
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
    private static final ThreadLocal<Capabilities> context = new NamedThreadLocal<>("capabilities context");

    private static final ThreadLocal<Capabilities> inheritableContext = new NamedInheritableThreadLocal<>("inheritable capabilities context");


    /**
     * Set.
     *
     * @param capabilities the capabilities
     */
    public static void set(Capabilities capabilities) {
        set(capabilities, false);
    }

    /**
     * Set.
     *
     * @param capabilities the capabilities
     * @param inheritable  the inheritable
     */
    public static void set(Capabilities capabilities, boolean inheritable) {
        if (capabilities != null) {
            if (inheritable) {
                inheritableContext.set(capabilities);
                context.remove();
            } else {
                context.set(capabilities);
                inheritableContext.remove();
            }
        } else {
            remove();
        }
    }

    /**
     * Get capabilities.
     *
     * @return the capabilities
     */
    public static Capabilities get() {
        Capabilities capabilities = context.get();
        if (capabilities == null) {
            capabilities = inheritableContext.get();
        }
        return capabilities;
    }

    /**
     * Remove.
     */
    public static void remove() {
        context.remove();
        inheritableContext.remove();
    }
}
