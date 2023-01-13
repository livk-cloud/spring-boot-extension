package com.livk.autoconfigure.useragent.browscap.support;

import com.blueconic.browscap.Capabilities;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.function.Function;

/**
 * <p>
 * ReactiveUserAgentContext
 * </p>
 *
 * @author livk
 */
@UtilityClass
public final class ReactiveUserAgentContextHolder {

    private static final Class<?> USER_AGENT_KEY = Capabilities.class;

    /**
     * Get mono.
     *
     * @return the mono
     */
    public Mono<Capabilities> get() {
        return Mono.deferContextual(Mono::just)
                .cast(Context.class)
                .filter(ReactiveUserAgentContextHolder::hasContext)
                .flatMap(ReactiveUserAgentContextHolder::getContext);
    }

    private static boolean hasContext(Context context) {
        return context.hasKey(USER_AGENT_KEY);
    }

    private static Mono<Capabilities> getContext(Context context) {
        return context.<Mono<Capabilities>>get(USER_AGENT_KEY);
    }

    /**
     * Clear context function.
     *
     * @return the function
     */
    public static Function<Context, Context> clearContext() {
        return (context) -> context.delete(USER_AGENT_KEY);
    }

    /**
     * With context context.
     *
     * @param capabilities the capabilities
     * @return the context
     */
    public static Context withContext(Mono<? extends Capabilities> capabilities) {
        return Context.of(USER_AGENT_KEY, capabilities);
    }

    /**
     * With context context.
     *
     * @param capabilities the capabilities
     * @return the context
     */
    public static Context withContext(Capabilities capabilities) {
        return withContext(Mono.just(capabilities));
    }
}
