package com.livk.autoconfigure.useragent.reactive;

import com.livk.commons.bean.Wrapper;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.function.Function;

/**
 * The type Reactive user agent context holder.
 *
 * @author livk
 */
public class ReactiveUserAgentContextHolder {

    private static final Class<?> USER_AGENT_KEY = ReactiveUserAgentContextHolder.class;

    /**
     * Get mono.
     *
     * @return the mono
     */
    public static Mono<Wrapper<?>> get() {
        return Mono.deferContextual(Mono::just)
                .cast(Context.class)
                .filter(ReactiveUserAgentContextHolder::hasContext)
                .flatMap(ReactiveUserAgentContextHolder::getContext);
    }

    private static boolean hasContext(Context context) {
        return context.hasKey(USER_AGENT_KEY);
    }

    private static Mono<Wrapper<?>> getContext(Context context) {
        return context.<Mono<Wrapper<?>>>get(USER_AGENT_KEY);
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
     * @param useragentWrapper the useragent wrapper
     * @return the context
     */
    public static Context withContext(Mono<? extends Wrapper<?>> useragentWrapper) {
        return Context.of(USER_AGENT_KEY, useragentWrapper);
    }

    /**
     * With context context.
     *
     * @param useragentWrapper the useragent wrapper
     * @return the context
     */
    public static Context withContext(Wrapper<?> useragentWrapper) {
        return withContext(Mono.just(useragentWrapper));
    }
}
